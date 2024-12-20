import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{Some}
import gleam/pair
import gleam/result
import gleam/set
import gleam/string
import pprint.{debug as d}
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

fn do_distances(map, queue, visited, res) {
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

      do_distances(
        map,
        list.append(queue, neighbours),
        set.insert(visited, x),
        res,
      )
    }
  }
}

fn distance(map, start, end) {
  let assert Ok(d) =
    do_distances(map, [start], set.new(), dict.from_list([#(start, 0)]))
    |> dict.get(end)

  d
}

fn manhattan(a: #(Int, Int), b: #(Int, Int)) -> Int {
  int.absolute_value(a.0 - b.0) + int.absolute_value(a.1 - b.1)
}

fn cheats(map: dict.Dict(#(Int, Int), String), length: Int) {
  map
  |> dict.filter(fn(_, v) { v != "#" })
  |> dict.keys
  |> list.flat_map(fn(x) {
    [#(0, 1), #(0, -1), #(1, 0), #(-1, 0)]
    |> list.map(fn(y) {
      let one = #(x.0 + y.0, x.1 + y.1)
      let two = #(one.0 + y.0, one.1 + y.1)

      #(one, two)
    })
  })
  |> list.filter_map(fn(cheat) {
    case dict.get(map, cheat.0), dict.get(map, cheat.1) {
      Ok("#"), Ok(v) if v != "#" -> Ok(cheat.0)
      _, _ -> Error(Nil)
    }
  })
  |> list.unique
}

fn part1(file) {
  let map = parse_input(file)
  let start = find_node(map, "S")
  let end = find_node(map, "E")

  let base = distance(map, start, end)
  let save = 100

  let cheats = cheats(map, 2)
  let cheats_length = list.length(cheats)

  cheats
  |> list.index_fold(0, fn(total, cheat, index) {
    // d(#(index, cheats_length))
    let dist =
      map
      |> dict.insert(cheat, ".")
      |> distance(start, end)

    case { base - dist } >= save {
      True -> total + 1
      _ -> total
    }
  })
  // |> list.length
  |> int.to_string
}

fn part2(file) {
  "todo"
}

pub fn main() {
  // let assert "todo" = part1("./resources/day20.example.txt")
  // let assert "todo" = part2("./resources/day20.example.txt")

  // let _ = part1("./resources/day20.example.txt")
  let file = "./resources/day20.input.txt"
  let part1 = part1(file)
  // let part2 = part2(file)
  io.println("Day 20 | Part 1: " <> part1)
  // io.println("Day 20 | Part 2: " <> part2)
}
