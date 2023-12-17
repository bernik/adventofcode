open Base 
open Stdio


let parse file = 
    In_channel.read_lines file
    |> List.fold ~init:([], []) ~f:(fun (times, distances) line -> 
        match Str.split (Str.regexp ":? +") line with
        | "Time"::tl -> (List.map tl ~f:Int.of_string, distances)
        | "Distance"::tl -> (times, List.map tl ~f:Int.of_string)
        | _ -> (times, distances)
    )
;;


let rec find_times_count max_time distance =
    let mid a b = 
        let d = b - a in
        if Int.is_positive (d % 2) 
        then a + d / 2 + 1
        else a + d / 2 
    in
    let rec binsearch a b = 
        if b - a <= 1 
        then max_time + 1 - (b * 2) 
        else 
            let m = mid a b in
            if (max_time - m) * m > distance 
            then binsearch a m
            else binsearch m b
    in 
    binsearch 0 max_time
;; 


let part1 file = 
    let times, distances = parse file in
    List.map2_exn times distances ~f:find_times_count
    |> List.fold ~init:1 ~f:( * )
    |> Int.to_string
;;

let part2 file = 
    let times, distances = parse file in
    let max_time = times |> List.map ~f:Int.to_string |> String.concat |> Int.of_string in
    let distance = distances |> List.map ~f:Int.to_string |> String.concat |> Int.of_string in
    Int.to_string (find_times_count max_time distance)
;;

let () = 
    printf "part1 example: %s\n" (part1 "day06/input.example.txt");
    printf "part1: %s\n"         (part1 "day06/input.txt");
    printf "part2 example: %s\n" (part2 "day06/input.example.txt");
    printf "part2: %s\n"         (part2 "day06/input.txt");
    ();

