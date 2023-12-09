open Core 
open Aoc


let parse_node line = 
    let re = Str.regexp "[0-9A-Z]+" in
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

let count_steps lrs nodes ~start ~stop =
    let step n = lrs.(Int.rem n (Array.length lrs)) in
    let rec aux node ~n = 
        if stop node
        then n
        else match step n with
        | 'L' -> Map.find_exn nodes node |> fst |> aux ~n:(n+1)   
        | 'R' -> Map.find_exn nodes node |> snd |> aux ~n:(n+1)   
        | _ -> failwith "Invalid direction"
    in
    aux start ~n:0 
;;


let part1 file = 
    let lrs, nodes = parse file in
    count_steps lrs nodes ~start:"AAA" ~stop:(String.equal "ZZZ")
    |> Int.to_string
;;


let ends_with s ~c =
    let last = (String.length s) - 1 in
    Char.equal (String.get s last) c
;;


let least_common_denominator xs = 
    let rec lcd init step ns =
        let found = List.for_all ns ~f:(fun n -> (Int.rem init n) = 0) in
        if found then init else lcd (init + step) step ns
    in
    let sorted = List.sort xs ~compare:(fun a b -> Int.compare b a) in
    match sorted with
    | n::[] -> n
    | dx::xs -> lcd dx dx xs
    | _ -> 0
;;


let part2 file =
    let lrs, all_nodes = parse file in
    Map.keys all_nodes 
    |> List.filter ~f:(ends_with ~c:'A')
    |> List.map ~f:(fun start -> 
        count_steps lrs all_nodes ~start ~stop:(ends_with ~c:'Z')
    )
    |> least_common_denominator
    |> Int.to_string
;; 



let () = 
    pf "part1 example: %s\n" (part1 "day08/input.example.txt");
    pf "part1: %s\n"         (part1 "day08/input.txt");
    pf "part2 example: %s\n" (part2 "day08/input.example2.txt");
    pf "part2: %s\n"         (part2 "day08/input.txt");
    ()
;;
