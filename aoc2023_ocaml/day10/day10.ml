open Base 
open Stdio

module Coords = struct
    module T = struct 
        type t = int * int 
        [@@deriving compare, sexp_of]
    end
    include T
    include Comparator.Make(T)
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
    | 'F' -> [south; east]
    | '|' -> [north; south]
    | '-' -> [west; east]
    | 'L' -> [north; east]
    | 'J' -> [north; west]
    | '7' -> [south; west]
    | _ -> [] 
;;


let bfs tiles start = 
    let visited = Set.empty (module Coords) in
    let rec aux queue visited = 
        match queue with
        | [] -> visited
        | _ -> 
            let visited' = List.fold queue 
                ~init:visited 
                ~f:(fun acc x -> Set.add acc x) 
            in
            let queue' = queue
                |> List.concat_map ~f:(next_coords tiles) 
                |> List.filter ~f:(fun x -> not @@ Set.mem visited' x )
            in 
            aux queue' visited' 
    in 
    aux [start] visited 
;;



let part1 file = 
    let tiles = parse file in 
    let (start_row, start_col as start) = find_start tiles in
    tiles.(start_row).(start_col) <- detect_start tiles start;
    let pipe = bfs tiles start |> Set.length in
    (pipe / 2) |> Int.to_string
;;


let part2 file = 
    let tiles = parse file in
    let height = Array.length tiles in
    let width = Array.length tiles.(0) in
    let (start_row, start_col as start) = find_start tiles in
    tiles.(start_row).(start_col) <- detect_start tiles start;
    let pipe = bfs tiles start in
    List.cartesian_product (List.range 0 height) (List.range 0 width)
    |> List.filter ~f:(fun x -> not @@ Set.mem pipe x )
    |> List.filter ~f:(fun (row, col) -> 
        let a, b = Set.filter pipe ~f:(fun (r, _) -> r = row)
        |> Set.fold ~init:(0,0) ~f:(fun (a, b) (p_row, p_col) -> 
            a,b
        )
        in 
        false
    )

;;


let () = 
    printf "part1 example: %s\n" (part1 "day10/input.example.txt");
    printf "part1 example2: %s\n" (part1 "day10/input.example2.txt");
    printf "part1: %s\n"         (part1 "day10/input.txt");
    (* printf "part2 example: %s\n" (part2 "day10/input.example.txt"); *)
    (* printf "part2: %s\n"         (part2 "day10/input.txt"); *)
    ();

