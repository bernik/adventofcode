open Base 
open Stdio
open Str
open Aoc

type rule_result = 
    | Accept 
    | Reject 
    | Goto of string
    | Skip

type part = { x:int; m: int; a: int; s:int }

module RegexParser = struct 
    let make_rule field pred value dest =
        let pred = match pred with 
            | ">" -> Int.(>)
            | "<" -> Int.(<)
            | _ -> failwith "Invalid predicate"
        in 
        let value = Int.of_string value in
        let dest = match dest with 
            | "A" -> Accept
            | "R" -> Reject
            | _ -> Goto dest
        in
        (fun part -> 
            let field_value = match field with 
                | "x" -> part.x
                | "m" -> part.m
                | "a" -> part.a
                | "s" -> part.s
                | _ -> failwith "Invalid field"
            in
            if pred field_value value
            then dest
            else Skip
        )
    ;;


    let parse_workflow s = 
        let _ = search_forward (regexp {|\([a-z]+\){\(.*\)}|}) s 0 in
        let name = matched_group 1 s in
        let rules = matched_group 2 s 
            |> String.split ~on:',' 
            |> List.map ~f:(fun x -> 
                if String.equal x "A" 
                then (fun _ -> Accept)

                else if String.equal x "R" 
                then (fun _ -> Reject)

                else if string_match (regexp "[a-z]+") s 0 
                then (fun _ -> Goto s)

                else 
                    let _ = string_match (regexp {|\(.+\)\([><]\)\([0-9]+\):\(.+\)|}) s 0 in
                    let field = matched_group 1 s in
                    let pred = matched_group 2 s in
                    let value = matched_group 3 s in
                    let dest = matched_group 4 s in
                    make_rule field pred value dest
            )
        in
        name, rules
    ;;

    let parse_workflows s = 
        String.split_lines s 
        |> List.map ~f:parse_workflow
        |> Map.of_alist_exn (module String)
    ;;


    let parse_parts s = 
        String.split_lines s
        |> List.map ~f:(fun line -> 
            let _ = string_match (regexp {|{x=\([0-9]+\),m=\([0-9]+\),a=\([0-9]+\),s=\([0-9]+\)}|}) line 0 in
            { x=(matched_group 1 line |> Int.of_string)
            ; m=(matched_group 2 line |> Int.of_string)
            ; a=(matched_group 3 line |> Int.of_string)
            ; s=(matched_group 4 line |> Int.of_string)
            }
        )
    ;;
end

module ParserHelpers = struct 
    open Angstrom

    let is_digit = function '0'..'9' -> true | _ -> false
    let is_lower = function 'a'..'z' -> true | _ -> false
    let integer = take_while1 is_digit
    let lcb = char '{'
    let rcb = char '}'
    let comma = char ','
    let colon = char ':'
end

module WorkflowParser = struct 
    open Angstrom
    open ParserHelpers

    let workflow_name = take_while1 is_lower 

    (* simple *)
    let accept = char 'A' >>| fun _ -> (fun _ -> Accept)
    let reject = char 'R' >>| fun _ -> (fun _ -> Reject)
    let goto = workflow_name >>| fun name -> (fun _ -> Goto name)

    (* conditional *)
    let _field = take 1  
        (* TODO: fail if next > or < *)
        >>| function 
        | "x" -> (fun part -> part.x) 
        | "m" -> (fun part -> part.m) 
        | "a" -> (fun part -> part.a) 
        | "s" -> (fun part -> part.s) 
        | f -> failwith ("Invalid field: " ^ f)

    let field = 
        let* f = take 1 in
        let* _ = peek_char >>= function 
            | Some '>' | Some '<' -> return f
            | _ -> fail "not field"
        in
        match f with
        | "x" -> return (fun part -> part.x) 
        | "m" -> return (fun part -> part.m) 
        | "a" -> return (fun part -> part.a) 
        | "s" -> return (fun part -> part.s) 
        | f -> failwith ("Invalid field: " ^ f)

    let pred = choice [ char '>'; char '<'] >>| function
        | '>' -> Int.(>)
        | '<' -> Int.(<)
        | _ -> failwith "Invalid predicate"

    let dest = choice [goto;accept;reject]

    let cond_rule = 
        lift4 
            (fun field pred value dest -> 
                (fun part -> 
                    if pred (field part) value 
                    then (dest part) 
                    else Skip 
                ))
            field
            pred 
            (integer >>| Int.of_string)
            (colon *> dest)
    ;;

    let rule = choice [accept;reject;cond_rule;goto]

    let _workflow = 
       let+ name = workflow_name 
       and+ _ = lcb 
       and+ rules = sep_by1 comma rule 
       and+ _ = rcb in
       name, rules

    let workflow = 
       let* name = workflow_name in
       let* rules = lcb *> sep_by1 comma rule <* rcb in
       return (name, rules)

    let parse s = 
        String.split_lines s 
        |> List.map ~f:(fun line -> 
            match parse_string ~consume:All workflow line with 
            | Ok (name, rules) -> name, rules
            | Result.Error error -> failwith error 
        )
        |> Map.of_alist_exn (module String)
    ;;
end 

module PartParser = struct 
    open Angstrom
    open ParserHelpers

    let part = lift4 (fun x m a s -> {x;m;a;s})
        (lcb *> string "x=" *> integer >>| Int.of_string)
        (comma *> string "m=" *> integer >>| Int.of_string)
        (comma *> string "a=" *> integer >>| Int.of_string)
        (comma *> string "s=" *> integer >>| Int.of_string)

    let parse s = String.split_lines s 
        |> List.map ~f:(parse_string ~consume:Prefix part)
        |> List.map ~f:Result.ok_or_failwith
end


let parse file =
    let input = In_channel.read_all file |> Str.split (Str.regexp "\n\n") in
    match input with 
    | [workflows_raw; parts_raw] -> 
        (WorkflowParser.parse workflows_raw), (PartParser.parse parts_raw)
    | _ -> failwith "Invalid input"
;;


let part1 file = 
    let workflows, parts = parse file in
    let rec process wf part = 
        let res = 
            List.find_map_exn wf ~f:(fun rule -> 
                match rule part with 
                | Skip -> None
                | x -> Some x
            )
        in 
        match res with 
        | Accept -> true
        | Reject -> false
        | Goto name -> process (Map.find_exn workflows name) part
        | _ -> failwith "Invalid workflow result"
    in
    List.filter parts ~f:(process (Map.find_exn workflows "in"))
    |> List.map ~f:(fun {x;m;a;s} -> x + m + a + s)
    |> List.reduce_exn ~f:( + )
    |> Int.to_string

;;
let part2 file = "part2";;


let () = 
    printf "part1 example: %s\n" (part1 "day19/input.example.txt");
    printf "part1: %s\n"         (part1 "day19/input.txt");
    printf "part2 example: %s\n" (part2 "day19/input.example.txt");
    printf "part2: %s\n"         (part2 "day19/input.txt");
    ();

