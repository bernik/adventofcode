let parse_input file =
  let input = open_in file in
  let lines = In_channel.input_lines input in
  let tbl = Hashtbl.create 0 in
  lines
  |> List.iteri (fun row line ->
    line
    |> String.to_seq
    |> Seq.iteri (fun col c -> Hashtbl.add tbl (row + 1, col + 1) (if c = '@' then 1 else 0)));
  tbl
;;

let height tbl = tbl |> Hashtbl.to_seq_keys |> Seq.map fst |> Seq.fold_left max 0
let width tbl = tbl |> Hashtbl.to_seq_keys |> Seq.map snd |> Seq.fold_left max 0

let neighbours tbl (row, col) =
  [ row + 1, col
  ; row - 1, col
  ; row, col + 1
  ; row, col - 1
  ; row + 1, col + 1
  ; row - 1, col + 1
  ; row + 1, col - 1
  ; row - 1, col - 1
  ]
  |> List.map (Hashtbl.find_opt tbl)
;;

let accessible input =
  Hashtbl.fold
    (fun pos v acc ->
       match v with
       | 1 ->
         let n_count =
           neighbours input pos
           |> List.fold_left
                (fun acc -> function
                   | Some n -> acc + n
                   | _ -> acc)
                0
         in
         if n_count < 4 then pos :: acc else acc
       | _ -> acc)
    input
    []
;;

let part1 file =
  let input = parse_input file in
  accessible input |> List.length
;;

let part2 file =
  let tbl = parse_input file in
  let rec aux acc =
    match accessible tbl with
    | [] -> acc
    | xs ->
      xs |> List.iter (fun x -> Hashtbl.replace tbl x 0);
      aux (acc + List.length xs)
  in
  aux 0
;;
