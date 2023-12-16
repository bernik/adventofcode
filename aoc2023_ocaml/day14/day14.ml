open Base
open Aoc
open Stdio

module Tiles = struct 
    module T = struct 
        type t = char list list
        [@@deriving compare, sexp_of, hash]


        let of_file path =
            In_channel.read_lines path
            |> List.filter ~f:(fun x -> not @@ String.is_empty x)
            |> List.map ~f:String.to_list
        ;;


        let total_load tiles = 
            List.rev tiles 
            |> List.foldi ~init:0 ~f:(fun idx acc xs -> 
                let n = xs
                    |> List.filter ~f:(Char.equal 'O') 
                    |> List.length 
                in
                acc + (idx + 1) * n
            )
        ;;


        let move tiles = 
            List.map tiles ~f:(fun xs ->
                let (res, acc) = List.fold xs ~init:([], []) ~f:(fun (res, acc) x -> 
                    match x with 
                    | 'O' -> res, 'O'::acc 
                    | '.' -> res, (acc @ ['.']) 
                    | '#' -> res @ acc @ ['#'], []
                    | x -> failwith ("Invalid value" ^ (Char.to_string x))
                ) in
                res @ acc
            )
        ;;

        let cycle tiles = 
            let t = List.transpose_exn in
            let r = List.rev in
            tiles 
            |> t |> move (* north *)
            |> t |> move (* west *)
            |> r |> t |> move (* south *)
            |> r |> t |> move (* east *)
            |> t |> r |> t |> r  (* init *)
        ;;

        let print tiles = 
            List.iter tiles ~f:(fun row -> print_endline (String.of_char_list row))
        ;;
    end 
    include T
    include Comparator.Make(T)
end


let part1 file = 
    Tiles.of_file file 
    |> List.transpose_exn
    |> Tiles.move
    |> List.transpose_exn
    |> Tiles.total_load
    |> Int.to_string
;;


let part2 file = 
    let init = Tiles.of_file file in
    let states = Hashtbl.create (module Tiles) in
    let rec aux states curr idx =  
        match Hashtbl.find states curr with 
        | Some start -> 
            let cycle_len = idx - start in
            let result_idx = (Int.rem (1000000000 - start) cycle_len) + start in
            let result = 
                Hashtbl.filter_mapi states ~f:(fun ~key ~data ->
                    if data = result_idx 
                    then Some (Tiles.total_load key) 
                    else None
                )
                |> Hashtbl.data
                |> List.hd_exn
            in
            result
        | None -> 
            Hashtbl.add_exn states ~key:curr ~data:idx;
            aux states (Tiles.cycle curr) (idx+1)
    in
    aux states init 0 |> Int.to_string
;;


let () = 
    pf "part1 example: %s\n" (part1 "day14/input.example.txt");
    pf "part1: %s\n"         (part1 "day14/input.txt");
    pf "part2 example: %s\n" (part2 "day14/input.example.txt");
    pf "part2: %s\n"         (part2 "day14/input.txt");
    ();

