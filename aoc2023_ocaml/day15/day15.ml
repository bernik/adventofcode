open Base 
open Stdio
open Aoc

let parse file =
    In_channel.read_all file
    |> String.rstrip
    |> String.split ~on:','
;;


let hash s = 
    String.to_list s
    |> List.fold ~init:0 ~f:(fun acc x ->
        Int.rem ((acc + Char.to_int x) * 17) 256
    )
;;

let part1 file = 
    parse file
    |> List.map ~f:hash
    |> List.reduce_exn ~f:( + )
    |> Int.to_string
;;

let add_update xs k v =
    if List.Assoc.mem xs k ~equal:String.equal 
    then 
        List.map xs ~f:(fun (kk, vv) ->
            if String.equal kk k 
            then k, v
            else kk, vv
        )
    else 
        List.Assoc.add xs k v ~equal:String.equal
;;


let part2 file = 
    let boxes = Array.create ~len:256 [] in
    parse file 
    |> List.map ~f:(fun x -> 
        match (String.split_on_chars x ~on:['=';'-']) with 
        | [label;""] -> `Remove label
        | [label;n] -> `Add (label, Int.of_string n)
        | _ -> failwith "Invalid instruction"
    )    
    |> List.fold ~init:boxes ~f:(fun boxes x -> 
        match x with 
        | `Remove label -> 
            let idx = hash label in
            let box = List.Assoc.remove boxes.(idx) label ~equal:String.equal  in
            boxes.(idx) <- box;
            boxes
        | `Add (label, n) -> 
            let idx = hash label in
            let box = add_update boxes.(idx) label n in
            boxes.(idx) <- box;
            boxes
    )
    |> Array.map ~f:List.rev
    |> Array.foldi ~init:0 ~f:(fun idx acc box ->
        let box_value = List.foldi box ~init:0 ~f:(fun n_idx acc (_, n) -> 
            acc + ((idx + 1) * (n_idx + 1) * n)
        ) in
        acc + box_value
    ) 
    |> Int.to_string
;;


let () = 
    pf "part1 example: %s\n" (part1 "day15/input.example.txt");
    pf "part1: %s\n"         (part1 "day15/input.txt");
    pf "part2 example: %s\n" (part2 "day15/input.example.txt");
    pf "part2: %s\n"         (part2 "day15/input.txt");
    ();

