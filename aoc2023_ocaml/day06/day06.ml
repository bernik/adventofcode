open Core 
open Aoc


let parse file = 
    In_channel.read_lines file
    |> List.fold ~init:([], []) ~f:(fun (times, distances) line -> 
        match Str.split (Str.regexp ":? +") line with
        | "Time"::tl -> (List.map tl ~f:Int.of_string, distances)
        | "Distance"::tl -> (times, List.map tl ~f:Int.of_string)
        | _ -> (times, distances)
    )
;;

let find_times_count max_time distance = 
    List.range ~stop:`inclusive 0 max_time 
    |> List.filter ~f:(fun time -> (max_time - time) * time > distance)
    |> List.length
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
    pf "part1 example: %s\n" (part1 "day06/input.example.txt");
    pf "part1: %s\n"         (part1 "day06/input.txt");
    pf "part2 example: %s\n" (part2 "day06/input.example.txt");
    pf "part2: %s\n"         (part2 "day06/input.txt");
    ();

