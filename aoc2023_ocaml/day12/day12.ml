open Base 
open Stdio

let parse file =
    In_channel.read_lines file 
    |> List.map ~f:(fun line -> 
        match String.split line ~on:' ' with
        | [a; b] -> 
        let chars = String.to_list a  in 
        let numbers = String.split b ~on:',' |> List.map ~f:Int.of_string in
        chars, numbers
        | _ -> failwith "Invalid line"
    )
;;


let rec foo xs curr groups acc =
    match xs, curr, groups with
    | [], None, [] -> 
        (* pf "ok: %s\n" acc;  *)
        1
    | [], Some 0, [] -> 
        (* pf "ok: %s\n" acc;  *)
        1
    | [], _, _ -> 0

    | '?'::xs, _, _ -> (foo ('.'::xs) curr groups acc) + (foo ('#'::xs) curr groups acc)

    | '#'::xs, None, [] -> 0
    | '#'::xs, None, g::gs -> foo xs (Some (g-1)) gs (acc ^ "#")
    | '#'::xs, Some 0, _ -> 0
    | '#'::xs, Some n, _ -> foo xs (Some (n-1)) groups (acc ^ "#")


    | '.'::xs, Some 0, _ -> foo xs None groups (acc ^ ".")
    | '.'::xs, None, _ -> foo xs None groups (acc ^ ".")
    
    | '.'::xs, _, [] -> foo xs curr [] (acc ^ ".")
    | '.'::xs, Some n, _ -> 0

    (* | '.'::xs, Some n, _ -> foo xs (Some n) groups (acc ^ ".") *)

    | _, _, _ -> 
        printf "%s, [%s]" 
            (String.of_list xs) 
            (groups |> List.map ~f:Int.to_string |> String.concat)
        ;
        failwith "invalid state"
;;


let () =
    let lines = parse "day12/input.example.txt" in
    List.iter lines ~f:(fun (xs, groups) -> 
        printf "xs: %s\n" (String.of_list xs);
        foo xs None groups ""  |> Int.to_string |> print_endline
    );
    (* foo ['#'] 0 [1] |> Int.to_string |> print_endline; *)
;;



let part1 file = 
    parse file
    |> List.fold ~init:0 ~f:(fun acc (xs, groups) -> 
        acc + (foo xs None groups "")
    )
    |> Int.to_string
;;


let part2 file = "part2";;


let () = 
    printf "part1 example: %s\n" (part1 "day12/input.example.txt");
    printf "part1: %s\n"         (part1 "day12/input.txt");
    (* 26037 too high *)
    printf "part2 example: %s\n" (part2 "day12/input.example.txt");
    printf "part2: %s\n"         (part2 "day12/input.txt");
    ();

