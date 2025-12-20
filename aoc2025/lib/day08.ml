module Junction = struct
  type t = int * int * int

  let cmp (ax, ay, az) (bx, by, bz) =
    match compare ax bx, compare ay by, compare az bz with
    | 0, 0, 0 -> 0
    | 0, 0, n -> n
    | 0, n, _ -> n
    | n, _, _ -> n
  ;;

  let distance : t -> t -> int =
    fun (x1, y1, z1) (x2, y2, z2) ->
    let dx = x1 - x2 in
    let dy = y1 - y2 in
    let dz = z1 - z2 in
    (dx * dx) + (dy * dy) + (dz * dz)
  ;;

  let of_string line =
    let numbers =
      line |> Utils.match_all {|[0-9]+|} |> List.map (fun (x, _, _) -> int_of_string x)
    in
    match numbers with
    | [ x; y; z ] -> x, y, z
    | _ -> failwith ("invalid line: " ^ line)
  ;;

  let pp ppf (x, y, z) = Fmt.pf ppf "(%d, %d, %d)" x y z
end

module Dsu = struct
  type t =
    { parent : (Junction.t, Junction.t) Hashtbl.t
    ; size : (Junction.t, int) Hashtbl.t
    }

  let make junctions =
    let parent = junctions |> List.to_seq |> Seq.map (fun x -> x, x) |> Hashtbl.of_seq in
    let size = junctions |> List.to_seq |> Seq.map (fun x -> x, 1) |> Hashtbl.of_seq in
    { parent; size }
  ;;

  let rec find x t =
    let parent = Hashtbl.find t.parent x in
    if parent = x then x else find parent t
  ;;

  let union a b t =
    let a = find a t in
    let b = find b t in
    if a = b
    then t
    else (
      let size_a = Hashtbl.find t.size a in
      let size_b = Hashtbl.find t.size b in
      let a, b = if size_a < size_b then b, a else a, b in
      Hashtbl.replace t.parent b a;
      Hashtbl.replace t.size a (size_a + size_b);
      t)
  ;;
end

let parse_input file : Junction.t list = Utils.read_lines file Junction.of_string

let lines junctions =
  let rec combinations = function
    | [] | [ _ ] -> []
    | x :: xs -> List.map (fun y -> x, y) xs @ combinations xs
  in
  combinations junctions
  |> List.sort (fun a b ->
    let da = Junction.distance (fst a) (snd a) in
    let db = Junction.distance (fst b) (snd b) in
    compare da db)
;;

let print_parent (dsu : Dsu.t) =
  dsu.parent
  |> Hashtbl.to_seq
  |> Seq.iter (fun (k, v) -> Fmt.pr "%a -> %a\n" Junction.pp k Junction.pp v)
;;

let print_size (dsu : Dsu.t) =
  dsu.size |> Hashtbl.to_seq |> Seq.iter (fun (k, v) -> Fmt.pr "%a -> %d\n" Junction.pp k v)
;;

let part1 file n =
  let junctions = parse_input file in
  let init = Dsu.make junctions in
  let lines = lines junctions |> List.take n in
  let res = List.fold_left (fun acc (a, b) -> Dsu.union a b acc) init lines in
  res.size
  |> Hashtbl.to_seq_values
  |> List.of_seq
  |> List.sort compare
  |> List.rev
  |> List.take 3
  |> List.fold_left ( * ) 1
;;

let part2 file =
  let junctions = parse_input file in
  let dsu = Dsu.make junctions in
  let len = List.length junctions in
  let (x1, _, _), (x2, _, _) =
    junctions
    |> lines
    |> List.drop_while (fun (a, b) ->
      let _ = Dsu.union a b dsu in
      dsu.size |> Hashtbl.to_seq_values |> Seq.exists (fun n -> n = len) |> not)
    |> List.hd
  in
  x1 * x2
;;
