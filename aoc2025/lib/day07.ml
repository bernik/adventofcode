module PairSet = Set.Make (struct
    type t = int * int

    let compare (x1, y1) (x2, y2) =
      match compare x1 x2 with
      | 0 -> compare y1 y2
      | n -> n
    ;;
  end)

let parse_input file =
  let tbl = Hashtbl.create 0 in
  open_in file
  |> In_channel.input_lines
  |> List.iteri (fun row line ->
    line |> String.to_seq |> Seq.iteri (fun col c -> Hashtbl.add tbl (row, col) c));
  tbl
;;

let start tbl = Hashtbl.fold (fun pos v acc -> if v = 'S' then pos else acc) tbl (0, 0)
let height tbl = Hashtbl.fold (fun (r, _) _ acc -> max r acc) tbl 0

let part1 file =
  let tbl = parse_input file in
  let init = PairSet.singleton (start tbl) in
  let rec aux state cnt = function
    | 0 -> cnt
    | n ->
      let state', cnt' =
        PairSet.fold
          (fun (row, col) (state, cnt) ->
             match Hashtbl.find tbl (row + 1, col) with
             | '^' -> PairSet.(state |> add (row + 1, col + 1) |> add (row + 1, col - 1)), cnt + 1
             | _ -> PairSet.add (row + 1, col) state, cnt)
          state
          (PairSet.empty, cnt)
      in
      aux state' cnt' (n - 1)
  in
  aux init 0 (height tbl)
;;

let part2 file =
  let tbl = parse_input file in
  let height = height tbl in
  let memory = Hashtbl.create 0 in
  let rec aux (row, col) =
    match Hashtbl.find_opt memory (row, col) with
    | Some n -> n
    | _ ->
      (match row, col with
       | row, col when row = height ->
         Hashtbl.add memory (row, col) 1;
         1
       | _ ->
         let res =
           match Hashtbl.find tbl (row + 1, col) with
           | '^' -> aux (row + 1, col + 1) + aux (row + 1, col - 1)
           | _ -> aux (row + 1, col)
         in
         Hashtbl.add memory (row, col) res;
         res)
  in
  aux (start tbl)
;;
