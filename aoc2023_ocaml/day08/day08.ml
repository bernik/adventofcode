open Core 
open Aoc


let parse_node line = 
    let re = Str.regexp "[A-Z]+" in
    let rec aux acc start =  
        try 
            let _  = Str.search_forward re line start in
            let s = Str.matched_string line in
            aux (s::acc) (Str.match_end ()) 
        with _ -> List.rev acc
    in
    aux [] 0
;;


let parse file = 
    let input = In_channel.read_all file |> Str.split (Str.regexp "\n\n") in
    match input with 
    | lrs::nodes::tl ->
        let lrs = String.to_array lrs in 
        let node_map = nodes 
            |> String.split ~on:'\n'
            |> List.fold ~init:[] ~f:(fun acc line ->
                match parse_node line with 
                | [a;b;c] -> (a, (b,c))::acc
                | _ -> acc
            )
            |> Map.of_alist_exn (module String)
        in
        lrs, node_map
    | _ -> failwith "Invalid input"
;;


let part1 file = 
    let lrs, nodes = parse file in
    let step n = lrs.(Int.rem n (Array.length lrs)) in
    let rec aux node ~n = 
        if String.(node = "ZZZ")
        then n
        else match step n with
        | 'L' -> Map.find_exn nodes node |> fst |> aux ~n:(n+1)   
        | 'R' -> Map.find_exn nodes node |> snd |> aux ~n:(n+1)   
        | _ -> failwith "Invalid direction"
    in
    aux "AAA" ~n:0 
    |> Int.to_string 
;; 


let part2 file = "part2";;

let () = 
    pf "part1 example: %s\n" (part1 "day08/input.example.txt");
    pf "part1: %s\n"         (part1 "day08/input.txt");
    pf "part2 example: %s\n" (part2 "day08/input.example.txt");
    pf "part2: %s\n"         (part2 "day08/input.txt");
    ()
;;
