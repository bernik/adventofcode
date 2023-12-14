open Base
open Aoc

let parse file = 
    Stdio.In_channel.read_lines file
    |> List.map ~f:String.to_list
;;


let part1 file = 
    parse file 
    |> List.transpose_exn
    |> List.map ~f:(fun xs -> 
        let l = List.length xs in
        List.foldi xs ~init:(0, l) ~f:(fun idx (acc, curr) x -> 
            match x with 
            | 'O' -> (acc + curr), (curr - 1)
            | '#' -> acc, (l - idx - 1)
            | _ -> acc, curr
        )
    )
    |> List.map ~f:fst
    |> List.reduce_exn ~f:( + )
    |> Int.to_string
;;

let part2 file = "part2";;


let () = 
    pf "part1 example: %s\n" (part1 "day14/input.example.txt");
    pf "part1: %s\n"         (part1 "day14/input.txt");
    pf "part2 example: %s\n" (part2 "day14/input.example.txt");
    pf "part2: %s\n"         (part2 "day14/input.txt");
    ();

