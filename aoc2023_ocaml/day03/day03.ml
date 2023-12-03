open Core 


let match_all pattern s = 
    let re = Str.regexp pattern in 
    let rec matches acc start = 
        try 
            let _ = Str.search_forward re s start in
            let res = Str.matched_string s, Str.match_beginning (), Str.match_end () - 1 in
            matches (res::acc) (Str.match_end ())
        with _ -> List.rev acc
    in 
    matches [] 0
;;


let belongs point rect = 
    let px, py = point in 
    let rx1, ry1, rx2, ry2 = rect in
    (rx1 <= px) && (px <= rx2) && (ry1 <= py) && (py <= ry2)
;; 


let parse file = 
    let numbers = 
        Aoc.read_lines file
        |> List.concat_mapi ~f:(fun y line ->
            match_all "[0-9]+" line
            |> List.map ~f:(fun (number, x1, x2) ->  
                Int.of_string number, (x1-1, y-1, x2+1, y+1)
            )
        ) 
    in
    let symbols = 
        Aoc.read_lines file
        |> List.concat_mapi ~f:(fun y line ->
            match_all "[^0-9\\.]+" line
            |> List.map ~f:(fun (s, x, _) -> s, (x, y))
        ) 
    in
    numbers, symbols
;; 


let part1 file = 
    let numbers, symbols = parse file in
    numbers
        |> List.filter ~f:(fun (_, rect) ->
            List.exists symbols ~f:(fun (_, point) -> belongs point rect)
        )
        |> List.fold ~init:0 ~f:(fun acc (n, _) -> acc + n)
        |> Int.to_string
;;


let part2 file = 
    let numbers, symbols = parse file in
    symbols
        |> List.filter ~f:(fun (s, _) -> String.equal s "*")
        |> List.map ~f:snd
        |> List.fold ~init:0 ~f:(fun acc (x, y) ->
            let adj_numbers = numbers 
                |> List.filter ~f:(fun (_, rect) -> belongs (x,y) rect )
                |> List.map ~f:fst
            in
            match adj_numbers with
            | [a; b] -> acc + (a * b)
            | _ -> acc
        )
        |> Int.to_string
;;



let () = 
    Printf.printf "part1 example: %s\n" (part1 "day03/input.example.txt");
    Printf.printf "part1: %s\n" (part1 "day03/input.txt");
    Printf.printf "part2 example: %s\n" (part2 "day03/input.example.txt");
    Printf.printf "part2: %s\n" (part2 "day03/input.txt");
    ();

