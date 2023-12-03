open Core 


let split2 ~on s =
    match String.split ~on s with 
    | [a; b] -> (a, b)
    | _ -> (s, "")



let parse_line line = 
    let game_str, buckets_str = split2 ~on:':' line in
    let _, game_id = split2 ~on:' ' game_str in
    let buckets = buckets_str 
        |> String.split ~on:';'
        |> List.map ~f:(fun bucket -> bucket
            |> String.split ~on:','  
            |> List.map ~f:String.strip 
            |> List.map ~f:(fun x -> 
                let n, c = split2 ~on:' ' x in
                c, (int_of_string n) 
            ) 
        )  
    in
    int_of_string game_id, buckets


let is_valid_bucket = 
    let valid = function
        | "blue", n -> n <= 14
        | "green", n -> n <= 13
        | "red", n -> n <= 12
        | _ -> false
    in 
    List.for_all ~f:valid


let is_valid_game = List.for_all ~f:is_valid_bucket


let part1 file = 
    Aoc.read_lines file
    |> List.map ~f:parse_line
    |> List.filter ~f:(fun x -> x |> snd |> is_valid_game)
    |> List.map ~f:fst
    |> List.fold_left ~init:0 ~f:(+) 
    |> string_of_int



let part2 file = 
    Aoc.read_lines file 
    |> List.map ~f:parse_line
    |> List.map ~f:snd
    |> List.map ~f:(fun buckets -> 
        let all = List.concat buckets in
        let max_color color = all
            |> List.filter ~f:(fun x -> String.equal (fst x) color)
            |> List.map ~f:snd
            |> List.fold ~init:0 ~f:Int.max
        in
        let red = max_color "red" in
        let blue = max_color "blue" in
        let green = max_color "green" in
        (red * blue * green)
    )
    |> List.fold ~init:0 ~f:( + )
    |> string_of_int



let () = 
    Printf.printf "part1 example: %s\n" (part1 "day02/input.example.txt");
    Printf.printf "part1: %s\n" (part1 "day02/input.txt");
    Printf.printf "part2 example: %s\n" (part2 "day02/input.example.txt");
    Printf.printf "part2: %s\n" (part2 "day02/input.txt");

