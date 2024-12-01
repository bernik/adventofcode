open Base 
open Stdio

let fold_tuples lst = 
    List.fold lst ~init:([], None) ~f:(fun (res, prev) n -> 
        match prev with 
        | Some m -> (m, n)::res, None
        | None -> res, Some n
    )
    |> fst
;;

let print_map m = 
    List.iter m ~f:(fun (a,b,c) -> 
        printf "(%d, %d, %d)\n" a b c
    )
;;


let combine_maps m1 m2 =
    print_endline "a:";
    print_map m1;
    print_endline "b:";
    print_map m2;
    print_endline "---------";
    List.concat_map m1 ~f:(fun (a, b, c) -> 
        let overlaps = m2
            |> List.filter ~f:(fun (x, y, _) -> a <= y && x <= b)
            |> List.concat_map ~f:(fun (x, y, z) -> 
                match a < x, b < y with
                | true, true   -> [(a, x, c);(x+1, b, z+c);(b + 1, y, z)]
                | false, false -> [(x, a, z);(a + 1, y, z + c);(y + 1, b, c);]
                | true, false  -> [(a, x, c);(x + 1, y, z + c);(y + 1, b, c)]
                | false, true  -> [(x, a, z);(a + 1, b, z + c);(b + 1, y, z)]
            )
        in 
        match overlaps with 
        | [] -> [a,b,c]
        | _  -> overlaps

    )
;;


let parse file ~part = 
    let seeds_parser = match part with
        | 1 -> (fun s -> s 
            |> List.map ~f:Int.of_string 
            |> List.map ~f:(fun x -> x,x))
        | 2 -> (fun s ->  
                s 
                |> List.map ~f:Int.of_string 
                |> fold_tuples
                |> List.map ~f:(fun (s, l) -> s, s + l - 1))
        | _ -> failwith "Invalid part"
    in
    let (seeds, (maps, _)) = 
        In_channel.read_lines file 
        |> List.fold ~init:([], ([], []))  ~f:(fun (state, (maps, curr_map) as acc) line ->
            let parts = Str.split (Str.regexp "[: ]+") line in
            match parts with 
            | "seeds"::tl -> (seeds_parser tl, (maps, curr_map)) 
            | a::b::c::_ -> 
                let dst_start = Int.of_string a in  
                let src_start = Int.of_string b in  
                let range_length = Int.of_string c in
                let m = (src_start, src_start + range_length - 1, dst_start - src_start ) in
                (state, (maps, m::curr_map))
            | [] when List.is_empty curr_map -> acc
            | [] -> (state, (curr_map::maps, []))
            | _ -> acc
        )
    in
    maps 
    |> List.rev
    |> List.map ~f:(List.sort ~compare:(fun (a, _, _) (b, _, _) -> Int.compare a b))
    |> List.iteri ~f:(fun idx x -> 
        (* print_endline ("map " ^ Int.to_string idx); *)
        (* x  *)
        (* |> List.iter  ~f:(fun (a,b,c) -> *)
            (* printf "(%d, %d, %d)\n" a b c *)
        (* ); *)
        ()
    );
    (* seeds, List.reduce_exn maps ~f:combine_maps  *)
    seeds, maps
;;

let convert = 
    let aux res seeds map = 
        match seeds, map with 
        | _, [] -> res @ seeds
        | [], _ -> res
        | (a,b)::tl, (x,y,dx)::tl2 -> 
            if a <= y && x <= b
            then []
            else []
    in
    aux [] 
;;


let solve file ~part = 
    let seeds, maps = parse file ~part in
    List.fold maps ~init:seeds ~f:convert
    |> List.map ~f:fst
    |> List.sort ~compare:Int.compare
    |> List.hd_exn 
    |> Int.to_string
;;


let part1 = solve ~part:1;;
let part2 = solve ~part:2;;

let () = 
    printf "part1 example: %s\n" (part1 "day05/input.example.txt");
    (* printf "part1: %s\n"         (part1 "day05/input.txt"); *)
    (* printf "part2 example: %s\n" (part2 "day05/input.example.txt"); *)
    (* printf "part2: %s\n"         (part2 "day05/input.txt"); *)
    ();

