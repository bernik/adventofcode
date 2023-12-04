open Core 

let print_int_list l =
    l
    |> List.map ~f:Int.to_string
    |> String.concat ~sep:", "
    |> Printf.printf "(%s)\n"
;;

let parse file =     
    Aoc.read_lines file
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


let part1 file = 
    parse file 
    |> List.map ~f:(fun (winning, my) ->
        let res = my
            |> List.filter ~f:(fun n -> List.mem winning n ~equal:Int.equal)
        in 
        match List.length res with 
        | 0 -> 0
        | n -> Int.pow 2 (n - 1)
    )
    |> List.fold ~init:0 ~f:( + )
    |> Int.to_string
;;

let part2 file = "part2"



let () = 
    Printf.printf "part1 example: %s\n" (part1 "day04/input.example.txt");
    Printf.printf "part1: %s\n" (part1 "day04/input.txt");
    (* Printf.printf "part2 example: %s\n" (part2 "day04/input.example.txt"); *)
    (* Printf.printf "part2: %s\n" (part2 "day04/input.txt"); *)
    ();

