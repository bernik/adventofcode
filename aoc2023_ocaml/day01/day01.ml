let parse_line pattern line = 
    let pattern = Str.regexp pattern in 
    let rec numbers acc start = 
        try 
            let _ = Str.search_forward pattern line start in
            let n = match Str.matched_string line with 
                | "one" -> "1"
                | "two" -> "2"
                | "three" -> "3"
                | "four" -> "4"
                | "five" -> "5"
                | "six" -> "6"
                | "seven" -> "7"
                | "eight" -> "8"
                | "nine" -> "9"
                | n -> n 
            in
            numbers (n:: acc) (Str.match_beginning () + 1) 
        with Not_found -> acc  
    in
    let xs = numbers [] 0 in
    let last = List.hd xs in 
    let first = xs |> List.rev |> List.hd in
    int_of_string (first ^ last)
;; 


let part1 file = 
    "day01/" ^ file 
    |> Aoc.read_lines 
    |> List.map (parse_line "[0-9]") 
    |> List.fold_left (+) 0


let part2 file = 
    let pattern = ["[0-9]"; "one"; "two"; "three"; "four"; "five"; "six"; "seven"; "eight"; "nine"]
        |> String.concat "\\|"
    in
    "day01/" ^ file 
    |> Aoc.read_lines 
    |> List.map (parse_line pattern) 
    |> List.fold_left (+) 0



let () = 
    Printf.printf "part1: %d \n" (part1 "input.example.txt");
    Printf.printf "part1: %d \n" (part1 "input.txt");
    Printf.printf "part2: %d \n" (part2 "input.example2.txt");
    Printf.printf "part2: %d \n" (part2 "input.txt");

