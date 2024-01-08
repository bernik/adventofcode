open Base
open Stdio

let parse file =
    In_channel.read_lines file 
    |> List.to_array
    |> Array.map ~f:String.to_array
;;


let distance (row1, col1) (row2, col2) =
    Int.abs (row2 - row1) + Int.abs (col2 - col1)
;;


let expanded_rows tiles =  
    Array.filter_mapi tiles ~f:(fun idx row -> 
        if Array.exists row ~f:(Char.equal '#') 
        then None 
        else Some idx
    )
;;


let expanded_cols tiles = expanded_rows @@ Array.transpose_exn tiles

let galaxies tiles =
    let height = Array.length tiles in 
    let width = Array.length tiles.(0) in
    let coords = List.cartesian_product 
        (List.range 0 height) 
        (List.range 0 width)
    in
    List.filter coords ~f:(fun (row, col) -> Char.equal '#' tiles.(row).(col))
;;


let rec permutations = function 
    | [] -> []
    | x::tl -> 
        let xs = List.map tl ~f:(fun y -> x, y) in
        xs @ permutations tl


let solve file ~n =
    let tiles = parse file in
    let exp_rows = expanded_rows tiles in
    let exp_cols = expanded_cols tiles in
    let galaxies = galaxies tiles in 
    let pairs = permutations galaxies in
    List.map pairs ~f:(fun ((row1, col1 as a), (row2, col2 as b)) -> 
        let dist = distance a b in 
        let empty_rows = exp_rows
            |> Array.filter ~f:(fun r -> 
                Int.between r ~low:(Int.min row1 row2) ~high:(Int.max row1 row2)
            ) 
            |> Array.length
        in
        let empty_columns = exp_cols
            |> Array.filter ~f:(fun c -> 
                Int.between c ~low:(Int.min col1 col2) ~high:(Int.max col1 col2)
            ) 
            |> Array.length
        in
        dist + (empty_rows * n) + (empty_columns * n)
    )
    |> List.reduce_exn ~f:(+)
    |> Int.to_string
;;


let part1 = solve ~n:1;;
let part2 = solve ~n:(1000000-1);;


let () = 
    printf "part1: %s \n" (part1 "day11/input.example.txt");
    printf "part1: %s \n" (part1 "day11/input.txt");
    printf "part2: %s \n" (part2 "day11/input.example.txt");
    printf "part2: %s \n" (part2 "day11/input.txt");

