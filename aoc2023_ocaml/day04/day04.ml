open Base 
open Stdio


let parse file =     
    In_channel.read_lines file
    |> List.map ~f:(Str.split (Str.regexp "[\\:\\|]"))
    |> List.map ~f:(fun x -> 
        match x with 
        | [_; winning; my] -> 
            ( winning |> Str.split (Str.regexp " +")|> List.map ~f:Int.of_string
            , my      |> Str.split (Str.regexp " +") |> List.map ~f:Int.of_string
            )
        | _ -> ([], [])
    )
;;

let matching_numbers (winning, my) = 
    my    
    |> List.filter ~f:(fun n -> List.mem winning n ~equal:Int.equal)
    |> List.length 
;;


let part1 file = 
    parse file 
    |> List.map ~f:matching_numbers
    |> List.fold ~init:0 ~f:(fun acc n -> 
        match n with
            | 0 -> acc 
            | x -> acc + Int.pow 2 (x - 1)
    )
    |> Int.to_string
;;

let part2 file =
    let cards = parse file |> List.map ~f:matching_numbers in
    let init = Array.create ~len:(List.length cards) 1 in
    cards 
    |> List.foldi ~init ~f:(fun idx acc n -> 
        match n with
        | 0 -> acc
        | _ -> 
            List.range (idx + 1) (idx + n + 1)
            |> List.iter ~f:(fun acc_idx -> acc.(acc_idx) <- (acc.(acc_idx) + acc.(idx)));
            acc
    )
    |> Array.fold ~init:0 ~f:( + )
    |> Int.to_string
;;


let () = 
    printf "part1 example: %s\n" (part1 "day04/input.example.txt");
    printf "part1: %s\n"         (part1 "day04/input.txt");
    printf "part2 example: %s\n" (part2 "day04/input.example.txt");
    printf "part2: %s\n"         (part2 "day04/input.txt");
    ();

