open Base 
open Stdio

let parse file = 
    In_channel.read_lines file
    |> List.map ~f:(fun line -> 
        let re = Str.regexp "[-0-9]+" in
        let rec aux acc start =
            try 
                let _ = Str.search_forward re line start in
                let s = Str.matched_string line in 
                aux (s::acc) (Str.match_end ())
            with _ -> acc |> List.rev |> List.map ~f:(Int.of_string)
        in
        aux [] 0
    )
;;


let differences lst =
    let rec aux acc lst = 
        if List.for_all lst ~f:(Int.equal 0) 
        then acc 
        else aux 
            (lst::acc) 
            (List.fold_right lst ~init:[] ~f:(fun x acc -> 
                match acc with 
                | [] -> x::acc
                | hd::tl -> x::(hd-x)::tl
            )
            |> List.tl_exn)
    in
    aux [] lst
;;



let () = 
    parse "day09/input.example.txt"
    |> List.map ~f:(fun x -> differences x |> List.length)
    |> List.reduce_exn ~f:Int.max
    (* |> print_int_list *)
    (* |> List.map ~f:(List.reduce_exn ~init:0 ~f:Int.max) *)
    |> Int.to_string
    |> print_endline
;;




let part1 file = "part1";;
let part2 file = "part2";;

let () = 
    printf "part1 example: %s\n" (part1 "day09/input.example.txt");
    printf "part1: %s\n"         (part1 "day09/input.txt");
    printf "part2 example: %s\n" (part2 "day09/input.example.txt");
    printf "part2: %s\n"         (part2 "day09/input.txt");
    ();

