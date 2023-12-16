open Base 
open Aoc

let parse file =
    Stdio.In_channel.read_lines file
    |> List.to_array 
    |> Array.map ~f:String.to_array
;;


module Beam = struct
    module T = struct 
        type t = string * int * int 
        [@@deriving compare, sexp_of]
    end
    include T
    include Comparator.Make(T)
end


let only_valid tiles beams = 
    let height = Array.length tiles in
    let width = Array.length tiles.(0) in
    List.filter beams ~f:(fun (_, row, col) -> 
        Int.between row ~low:0 ~high:(height-1)
        &&
        Int.between col ~low:0 ~high:(width-1)
    )
;;


let move tiles beams = 
    List.map beams ~f:(function 
        | "left", row, col -> "left", row, col - 1
        | "right", row, col -> "right", row, col + 1
        | "up", row, col -> "up", row-1, col
        | "down", row, col -> "down", row+1, col
        | _ -> failwith "Invalid direction"
    )
    |> only_valid tiles
;;

let collisions tiles beams =
    List.concat_map beams ~f:(fun (direction, row, col as beam) -> 
        match direction, tiles.(row).(col) with
        | "left", '-' | "right", '-' -> [beam]
        | "right", '|' | "left", '|' -> [("down", row, col); ("up",row, col)]

        | "right", '/' -> [("up", row, col)]
        | "right", '\\' -> [("down", row, col)]

        | "left", '/' -> [("down", row, col)]
        | "left", '\\' -> [("up", row, col)]

        | "up", '|' | "down", '|' -> [beam]
        | "up", '-' | "down", '-' -> [("left", row, col); ("right", row, col)]

        | "up", '/' -> [("right", row, col)]
        | "up", '\\' -> [("left", row, col)]

        | "down", '/' -> [("left", row, col)]
        | "down", '\\' -> [("right", row, col)]

        | _, '.' -> [beam]
        | _, _ -> failwith "Invalid collision"
        | exception _ -> []
    )
    |> only_valid tiles
;;


let print_tiles tiles visited =
    Set.iter visited ~f:(fun (_, row, col) -> 
        tiles.(row).(col) <- '#' 
    );
    Array.iter tiles ~f:(fun xs ->
        Stdio.print_endline (xs |> List.of_array |> String.of_char_list)
    )
;;


let solve tiles start =
    let visited = Set.empty (module Beam) in
    let rec aux queue visited = 
        match queue with
        | [] -> visited
        | queue -> 
            let new_visited = List.fold 
                queue 
                ~init:visited 
                ~f:(fun acc x -> Set.add acc x)
            in
            let new_queue = queue 
                |> collisions tiles
                |> move tiles 
                |> List.filter ~f:(fun x -> not @@ Set.mem new_visited x)
            in
            aux new_queue new_visited
    in
    aux [start] visited 
    |> Set.map (module Int) ~f:(fun (_, row, col) -> 1000 * row + col)
    |> Set.length
;;


let part1 file = 
    let start = "right", 0, 0 in
    let tiles = parse file in
    solve tiles start |> Int.to_string
;;

let part2 file = 
    let tiles = parse file in
    let height = Array.length tiles in
    let width = Array.length tiles.(0) in
    List.concat [
        List.range 0 height |> List.map ~f:(fun row -> "right", row, 0);
        List.range 0 height |> List.map ~f:(fun row -> "left", row, width-1);
        List.range 0 width |> List.map ~f:(fun col -> "down", 0, col);
        List.range 0 width |> List.map ~f:(fun col -> "up", height-1, col);
    ]
    |> List.fold ~init:0 ~f:(fun acc start -> 
        Int.max acc (solve tiles start) 
    )
    |> Int.to_string
;;


let () = 
    pf "part1 example: %s\n" (part1 "day16/input.example.txt");
    pf "part1: %s\n"         (part1 "day16/input.txt");
    pf "part2 example: %s\n" (part2 "day16/input.example.txt");
    pf "part2: %s\n"         (part2 "day16/input.txt");
    ();

