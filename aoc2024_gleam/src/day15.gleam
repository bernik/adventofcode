import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import pprint.{debug as d}
import simplifile

fn parse_input(file, expanded) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(#(map, moves)) = content |> string.split_once(on: "\n\n")
  let map =
    map
    |> string.split(on: "\n")
    |> list.index_map(fn(line, row) {
      line
      |> string.split(on: "")
      |> list.flat_map(fn(x) {
        case expanded {
          False -> [x]
          True -> {
            case x {
              "@" -> ["@", "."]
              "O" -> ["[", "]"]
              _ -> [x, x]
            }
          }
        }
      })
      |> list.index_map(fn(x, col) { #(#(row, col), x) })
    })
    |> list.flatten
    |> dict.from_list

  let moves =
    moves
    |> string.split(on: "\n")
    |> list.flat_map(fn(line) {
      line
      |> string.split(on: "")
      |> list.map(fn(c) {
        case c {
          "^" -> #(-1, 0)
          "v" -> #(1, 0)
          ">" -> #(0, 1)
          "<" -> #(0, -1)
          _ -> panic as "invalid instruction"
        }
      })
    })

  #(map, moves)
}

fn find_start(map) {
  let assert Ok(#(pos, _)) =
    map
    |> dict.to_list
    |> list.find(fn(x) {
      let #(_, c) = x
      c == "@"
    })
  pos
}

fn add_pairs(a: #(Int, Int), b: #(Int, Int)) -> #(Int, Int) {
  #(a.0 + b.0, a.1 + b.1)
}

fn can_be_pushed(map, cur, move) {
  let next = add_pairs(cur, move)
  let cur_value = dict.get(map, cur) |> result.unwrap("#")
  let next_value = dict.get(map, next) |> result.unwrap("#")
  let horizontal = move == #(0, -1) || move == #(0, 1)
  let vertical = !horizontal

  case cur_value, next_value {
    // part 1
    "O", "." -> True
    "O", "O" -> can_be_pushed(map, next, move)

    // part 2
    "[", "." | "]", "." if horizontal -> True
    "[", "]" | "]", "[" if horizontal -> can_be_pushed(map, next, move)

    "]", _ if vertical -> can_be_pushed(map, add_pairs(cur, #(0, -1)), move)

    "[", _ if vertical -> {
      let next2 = add_pairs(next, #(0, 1))
      let next2_value = dict.get(map, next2) |> result.unwrap("#")
      case next_value, next2_value {
        "#", _ | _, "#" -> False
        ".", "." -> True
        "[", "]" -> can_be_pushed(map, next, move)
        "]", "[" ->
          can_be_pushed(map, next, move) && can_be_pushed(map, next2, move)
        "]", "." -> can_be_pushed(map, next, move)
        ".", "[" -> can_be_pushed(map, next2, move)
        _, _ -> {
          d(#(next_value, next2_value))
          panic as "huh?"
        }
      }
    }
    _, _ -> False
  }
}

fn push(map, cur, move) {
  let next = add_pairs(cur, move)
  let cur_value = dict.get(map, cur) |> result.unwrap("#")

  case can_be_pushed(map, cur, move) {
    False -> map
    True ->
      case cur_value {
        "O" ->
          map
          |> push(next, move)
          |> dict.insert(cur, ".")
          |> dict.insert(next, "O")

        "]" -> push(map, add_pairs(cur, #(0, -1)), move)

        "[" -> {
          let right = add_pairs(cur, #(0, 1))
          let next2 = add_pairs(right, move)

          case move {
            // prevent infinite loop
            #(0, 1) ->
              map
              |> push(next2, move)
              |> dict.insert(cur, ".")
              |> dict.insert(right, "[")
              |> dict.insert(next2, "]")

            #(0, -1) ->
              map
              |> push(next, move)
              |> dict.insert(next, "[")
              |> dict.insert(cur, "]")
              |> dict.insert(right, ".")

            _ ->
              map
              |> push(next, move)
              |> push(next2, move)
              |> dict.insert(cur, ".")
              |> dict.insert(right, ".")
              |> dict.insert(next, "[")
              |> dict.insert(next2, "]")
          }
        }
        _ -> map
      }
  }
}

fn do_solve(map, moves, cur) {
  case moves {
    [] -> map
    [move, ..rest] -> {
      let next = add_pairs(cur, move)
      // let map = try_push(map, next, move) |> result.unwrap(map)
      let map = push(map, next, move)

      case dict.get(map, next) {
        Ok(".") -> do_solve(map, rest, next)
        _ -> do_solve(map, rest, cur)
      }
    }
  }
}

/// for debug
// fn print_map(map, cur) {
//   let h = map |> dict.keys |> list.map(pair.first) |> list.fold(0, int.max)
//   let w = map |> dict.keys |> list.map(pair.second) |> list.fold(0, int.max)

//   list.range(0, h)
//   |> list.each(fn(row) {
//     list.range(0, w)
//     |> list.map(fn(col) {
//       case #(row, col) == cur {
//         True -> Ok("@")
//         False -> dict.get(map, #(row, col))
//       }
//     })
//     |> result.values
//     |> string.concat
//     |> io.println
//   })

//   map
// }

fn solve(file, expanded) {
  let #(map, moves) = parse_input(file, expanded)
  let start = find_start(map)

  do_solve(map |> dict.insert(start, "."), moves, start)
  |> dict.filter(fn(_, v) { v == "O" || v == "[" })
  |> dict.keys
  |> list.fold(0, fn(acc, x) { acc + 100 * x.0 + x.1 })
  |> int.to_string
}

pub fn main() {
  // let file = "./resources/day15.example2.txt"
  let file = "./resources/day15.input.txt"
  let part1 = solve(file, False)
  let part2 = solve(file, True)

  io.println("Day 15 | Part 1: " <> part1)
  io.println("Day 15 | Part 2: " <> part2)
}
