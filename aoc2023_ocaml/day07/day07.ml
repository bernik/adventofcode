open Core 
open Aoc


let parse_hand s = 
    String.to_list s
    |> List.map ~f:(function 
        | 'A' -> 14
        | 'K' -> 13
        | 'Q' -> 12
        | 'J' -> 11
        | 'T' -> 10
        | x -> x |> Char.to_string |> Int.of_string
    )
;;


let parse file =
    In_channel.read_lines file
    |> List.map ~f:(fun line ->
        let hand, bid = String.lsplit2_exn ~on:' ' line in
        parse_hand hand, Int.of_string bid
    )
;;


let freqs lst = 
    let res, part = 
        lst
        |> List.sort ~compare:Int.compare
        |> List.fold ~init:([], None) ~f:(fun (acc, part) n ->
            match part with
            | Some (c, count) when Int.equal c n -> acc, Some (c, count + 1)
            | Some (c, count) -> ((c, count)::acc), Some (n, 1)
            | None -> acc, Some (n, 1)
        )
    in 
    (Option.value_exn part)::res
    |> List.sort ~compare:(fun (a1, b1) (a2, b2) ->
        let b = Int.compare b2 b1 in 
        if Int.equal 0 b 
        then Int.compare a2 a1
        else b
    )
;;


let hand_type hand = 
    let jokers = match freqs hand |> List.find ~f:(fun (c, _) -> c = 1) with
        | Some (_, n) -> n
        | None -> 0
    in
    let xs = freqs hand 
        |> List.filter ~f:(fun (c, _) -> c <> 1)
        |> List.mapi ~f:(fun idx (_, n) ->
            if idx = 0
            then n + jokers
            else n
        ) 
    in
    match xs with 
    | [5] -> 6
    | [4;1] -> 5
    | [3;2] -> 4
    | [3;1;1] -> 3
    | [2;2;1] -> 2
    | [2;1;1;1] -> 1
    | [] -> 1 + jokers
    | _ -> 0
;;


let rec higher_card hand1 hand2 = 
    match hand1, hand2 with
    | [], [] -> 0
    | a::atl, b::btl when a = b -> higher_card atl btl
    | a::_, b::_  -> Int.compare a b
    | _ -> failwith "Invalid higher card"
;;



let solve hands = 
    hands
    |> List.sort ~compare:(fun (hand1, _) (hand2, _) -> 
        let t1 = hand_type hand1 in
        let t2 = hand_type hand2 in
        let r = Int.compare t1 t2 in
        if r = 0 
        then higher_card hand1 hand2 
        else r
    )
    |> List.mapi ~f:(fun rank (_, bid) -> (rank + 1) * bid)
    |> List.reduce_exn ~f:( + )
    |> Int.to_string
;;

let part1 file = parse file |> solve;;
let part2 file = parse file 
    |> List.map ~f:(fun (hand, bid) -> 
        let with_jokers = List.map hand ~f:(fun n -> 
            if n = 11 then 1 else n
        ) 
        in
        with_jokers, bid
    )
    |> solve
;;


let () = 
    pf "part1 example: %s\n" (part1 "day07/input.example.txt");
    pf "part1: %s\n"         (part1 "day07/input.txt");
    pf "part2 example: %s\n" (part2 "day07/input.example.txt");
    pf "part2: %s\n"         (part2 "day07/input.txt");
    ();

