open Core 


let match_all pattern s = 
    let re = Str.regexp pattern in 
    let rec matches acc start = 
        try 
            let _ = Str.search_forward re s start in
            let res = Str.matched_string s, Str.match_beginning (), Str.match_end () in
            matches (res::acc) (Str.match_end () + 1)
        with _ -> List.rev acc
    in 
    matches [] 0
;;


let string_slice s ~from ~to_ = 
    let max_length = String.length s in
    let pos = Int.max from 0 in
    let len = (Int.min to_ max_length) - pos in
    String.sub s ~pos ~len
;;


let has_adjacent_symbols schema row_index (num, x1, x2) =
    (* print_endline "--------------"; *)
    (* Printf.printf "%d: %s, %d, %d\n" row_index num x1 x2; *)
    let schema_length = Array.get schema 0 |> String.length in
    let check_rows = 
        [row_index - 1; row_index; row_index + 1]
        |> List.map ~f:(fun idx -> try Array.get schema idx with _ -> String.make schema_length '.')
        |> List.map ~f:(fun s -> string_slice s ~from:(x1 - 1) ~to_:(x2 + 1))
    in
    check_rows |> String.concat ~sep:"\n" |> print_endline;
    check_rows
    |> List.exists ~f:(fun row -> 
        let res = 
            try
                let _ =  Str.search_forward (Str.regexp "[^0-9\\.]") row 0 in
                true
            with _ -> false
        in
        (* let res = Str.string_partial_match (Str.regexp "[^0-9\\.]") row 0 in *)
        (* Printf.printf "%s -> %b\n" row res; *)
        res 
    )
;;


let part1 file = 
    let schema = Aoc.read_lines file |> List.to_array in
    schema 
    |> Array.concat_mapi ~f:(fun idx line ->
        match_all "[0-9]+" line 
        |> List.filter ~f:(fun found_number -> 
            has_adjacent_symbols schema idx found_number 
        )
        |> List.map ~f:(fun (n, _, _) -> Int.of_string n)
        |> List.to_array
    )
    |> Array.fold ~init:0 ~f:( + )
    |> Int.to_string 
;;




let part2 file = "foo"


let () = 
    Printf.printf "part1 example: %s\n" (part1 "day03/input.example.txt");
    Printf.printf "part1: %s\n" (part1 "day03/input.txt");
    (* Printf.printf "part2 example: %s\n" (part2 "day03/input.example.txt"); *)
    (* Printf.printf "part2: %s\n" (part2 "day03/input.txt"); *)
    ();

