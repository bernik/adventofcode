import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{Some}
import gleam/set
import gleam/string
import utils

fn parse_input(file) {
  utils.read_lines(file)
  |> list.filter(fn(line) { line != "" })
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(x, col) { #(#(row, col), x) })
  })
  |> list.flatten
  |> dict.from_list
}

fn find_node(map, node) {
  let assert Ok(#(pos, _)) =
    map
    |> dict.to_list
    |> list.find(fn(x) { x.1 == node })

  pos
}

fn neighbours(map, node: #(Int, Int)) {
  [#(0, 1), #(0, -1), #(1, 0), #(-1, 0)]
  |> list.filter_map(fn(x) {
    let n = #(x.0 + node.0, x.1 + node.1)
    case dict.get(map, n) {
      Ok(".") | Ok("E") -> Ok(n)
      _ -> Error(Nil)
    }
  })
}

fn all_distances(map, queue, visited, res) {
  case queue {
    [] -> res
    [x, ..queue] -> {
      let assert Ok(cur) = dict.get(res, x)
      let neighbours =
        neighbours(map, x) |> list.filter(fn(x) { !set.contains(visited, x) })

      let res =
        neighbours
        |> list.fold(res, fn(acc, n) {
          let nv = cur + 1
          acc
          |> dict.upsert(n, fn(v) {
            case v {
              Some(v) if v < nv -> nv
              Some(v) -> v
              _ -> nv
            }
          })
        })

      all_distances(
        map,
        list.append(queue, neighbours),
        set.insert(visited, x),
        res,
      )
    }
  }
}

fn manhattan(a: #(Int, Int), b: #(Int, Int)) -> Int {
  int.absolute_value(a.0 - b.0) + int.absolute_value(a.1 - b.1)
}

fn cheats(map, length) {
  map
  |> dict.filter(fn(_, v) { v == "." || v == "E" || v == "S" })
  |> dict.keys
  |> list.combination_pairs
  |> list.filter(fn(pair) {
    let #(a, b) = pair
    let m = manhattan(a, b)

    2 <= m && m <= length
  })
}

fn solve(file, cheats_length, save_threshold) {
  let map = parse_input(file)
  let start = find_node(map, "S")
  let cheats = cheats(map, cheats_length)
  let distances =
    all_distances(map, [start], set.new(), dict.from_list([#(start, 0)]))

  cheats
  |> list.filter(fn(cheat) {
    let #(a, b) = cheat
    let m = manhattan(a, b)
    let assert Ok(av) = dict.get(distances, a)
    let assert Ok(bv) = dict.get(distances, b)

    let save = int.absolute_value(bv - av) - m

    save >= save_threshold
  })
  |> list.length
  |> int.to_string
}

pub fn main() {
  let file = "./resources/day20.input.txt"
  let part1 = solve(file, 2, 100)
  let part2 = solve(file, 20, 100)
  io.println("Day 20 | Part 1: " <> part1)
  io.println("Day 20 | Part 2: " <> part2)
}
