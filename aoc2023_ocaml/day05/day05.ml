open Core 
open Aoc

let fold_tuples lst = 
    List.fold lst ~init:([], None) ~f:(fun (res, prev) n -> 
        match prev with 
        | Some m -> (m, n)::res, None
        | None -> res, Some n
    )
    |> fst


let solve file ~part = 
    let seeds_parser = match part with
        | 1 -> (fun s -> s |> List.map ~f:Int.of_string)
        | 2 -> (fun s ->  
                s 
                |> List.map ~f:Int.of_string 
                |> fold_tuples
                |> List.concat_map ~f:(fun (start, length) -> 
                    List.range start (start + length) ~stop:`exclusive
                )
            )
        | _ -> failwith "Invalid part"
    in
    In_channel.read_lines file 
    |> List.fold ~init:([], [])  ~f:(fun (state, maps as acc) line ->
        let parts = Str.split (Str.regexp "[: ]+") line in
        match parts with 
        | "seeds"::tl -> (seeds_parser tl, maps) 
        | a::b::c::_ -> 
            let dst_start = Int.of_string a in  
            let src_start = Int.of_string b in  
            let range_length = Int.of_string c in  
            (state, ((src_start, dst_start, range_length)::maps))
        | [] -> 
            let new_state = state 
                |> List.map ~f:(fun n -> 
                    let m = List.find maps ~f:(fun (src_start, dst_start, range_length) -> 
                        Int.between n ~low:src_start ~high:(src_start + range_length - 1)
                    )
                    in
                    match m with 
                    | Some (src_start, dst_start, _) -> (dst_start + (n - src_start))
                    | None -> n
                )
            in
            (new_state, [])
        | _ -> acc
    )
    |> fst
    |> List.sort ~compare:Int.compare
    |> List.hd_exn 
    |> Int.to_string
;;


let part1 = solve ~part:1;;
let part2 = solve ~part:2;;

let () = 
    pf "part1 example: %s\n" (part1 "day05/input.example.txt");
    pf "part1: %s\n"         (part1 "day05/input.txt");
    pf "part2 example: %s\n" (part2 "day05/input.example.txt");
    pf "part2: %s\n"         (part2 "day05/input.txt");
    ();

