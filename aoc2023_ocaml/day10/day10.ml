open Core 
open Aoc

module Coords = struct
    include Tuple.Make (Int) (Int)
    include Tuple.Hashable (Int) (Int)
    include Tuple.Comparable (Int) (Int)

    let print (row, col) = pf "(%d, %d)\n" row col
end


let parse file = 
    In_channel.read_lines file
    |> List.to_array
    |> Array.map ~f:String.to_array
;;


let find_start m =
    Array.find_mapi_exn m ~f:(fun row cs -> 
        match Array.findi cs ~f:(fun _ c -> Char.equal 'S' c) with 
        | Some (col, _) -> Some (row, col)
        | None -> None
    )
;;


let detect_start m (row, col) =
    let n = match m.(row-1).(col) with
        | 'F' | '|' | '7' -> true
        | _ -> false
        | exception _ -> false
    in
    let s = match m.(row+1).(col) with
        | '|' | 'L' | 'J' -> true
        | _ -> false
        | exception _ -> false
    in
    let w = match m.(row).(col-1) with
        | 'F' | '-' | 'L' -> true
        | _ -> false
        | exception _ -> false
    in
    let e = match m.(row).(col+1) with
        | 'J' | '-' | '7' -> true
        | _ -> false
        | exception _ -> false
    in
    match n,s,w,e with
    | false, true, false, true -> 'F'
    | true, true, false, false -> '|'
    | false, false, true, true -> '-'
    | true, false, false, true -> 'L'
    | true, false, true, false -> 'J'
    | false, true, true, false -> '7'
    | _ -> failwith "Invalid start"
;;


 let next_coords m (row, col) =
    let north = row-1, col in
    let south = row+1, col in
    let west = row, col-1 in
    let east = row, col+1 in
    match m.(row).(col) with 
    (* | 'S' -> [] *)
    | 'F' -> [south; east]
    | '|' -> [north; south]
    | '-' -> [west; east]
    | 'L' -> [north; east]
    | 'J' -> [north; west]
    | '7' -> [south; west]
    | _ -> [] 
;;


let print_q q = 
    print_endline "queue:";
    List.iter q ~f:(fun (row, col) -> pf "(%d, %d)\n" row col)
;;


let print_visited visited = 
    print_endline "visited:";
    Hashtbl.to_alist visited
    |> List.sort ~compare:(fun (_, v1) (_, v2) -> Int.compare v1 v2 )
    |> List.iter ~f:(fun ((row, col), v) -> 
        pf "(%d, %d) => %d\n" row col v
    ) 
;;


let bfs m start = 
    let visited = Hashtbl.create (module Coords) in
    let rec aux q n = 
        match q with
        | [] -> Hashtbl.data visited |> List.reduce_exn ~f:Int.max
        | _ -> 
            List.iter q ~f:(fun key -> Hashtbl.set visited ~key ~data:n);
            let xs = q
                |> List.concat_map ~f:(next_coords m) 
                |> List.filter ~f:(fun x -> not @@ Hashtbl.mem visited x )
            in 
            aux xs (n+1)
    in 
    let res = aux [start] 0 in
    res
;;



let part1 file = 
    let m = parse file in 
    let (start_row, start_col as start) = find_start m in
    m.(start_row).(start_col) <- detect_start m start;
    bfs m start |> Int.to_string
;;

let part2 file = "part2";;


let () = 
    pf "part1 example: %s\n" (part1 "day10/input.example.txt");
    pf "part1 example2: %s\n" (part1 "day10/input.example2.txt");
    pf "part1: %s\n"         (part1 "day10/input.txt");
    (* pf "part2 example: %s\n" (part2 "day10/input.example.txt"); *)
    (* pf "part2: %s\n"         (part2 "day10/input.txt"); *)
    ();

