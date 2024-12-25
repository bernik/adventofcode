import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/set
import gleam/string
import utils.{read_lines}

fn parse_input(file) {
  read_lines(file)
  |> list.index_map(fn(line, row) {
    line
    |> string.split(on: "")
    |> list.index_map(fn(x, col) { #(#(row, col), x) })
  })
  |> list.flatten
  |> dict.from_list
}

fn neighbours(map, x: #(Int, Int)) {
  let assert Ok(v) = dict.get(map, x)
  [#(-1, 0), #(1, 0), #(0, -1), #(0, 1)]
  |> list.map(fn(dx) { #(x.0 + dx.0, x.1 + dx.1) })
  |> list.filter(fn(y) {
    case dict.get(map, y) {
      Ok(vv) -> v == vv
      _ -> False
    }
  })
}

fn collect_region(map, queue, region) {
  case queue {
    [] -> region
    [x, ..xs] -> {
      case set.contains(region, x) {
        True -> collect_region(map, xs, region)
        False -> {
          let neighbours = neighbours(map, x)
          collect_region(
            map,
            list.append(xs, neighbours),
            set.insert(region, x),
          )
        }
      }
    }
  }
}

fn do_regions(map, queue, result) {
  case queue {
    [] -> result
    [x, ..] -> {
      let region = collect_region(map, [x], set.new())
      let queue =
        queue |> list.filter(fn(x) { False == set.contains(region, x) })
      do_regions(map, queue, [set.to_list(region), ..result])
    }
  }
}

fn regions(map) {
  let queue = map |> dict.keys
  do_regions(map, queue, [])
}

fn perimeter(map, region) {
  list.length(region)
  * 4
  - {
    region
    |> list.combination_pairs
    |> list.filter(fn(ab) { neighbours(map, ab.0) |> list.contains(ab.1) })
    |> list.length
  }
  * 2
}

/// for debug(regions, map)
// fn print_regions(regions, map) {
//   regions
//   |> list.map(fn(region) {
//     let label =
//       region |> list.map(dict.get(map, _)) |> result.values |> list.unique
//     #(label, list.length(region))
//   })
//   |> pprint.debug
// }

fn solve(file, price) {
  let map = parse_input(file)

  map
  |> regions
  |> list.map(price(map, _))
  |> int.sum
  |> int.to_string
}

fn part1(file) {
  solve(file, fn(map, region) { list.length(region) * perimeter(map, region) })
}

fn part2(file) {
  solve(file, fn(_, region) {
    let corners =
      region
      |> list.map(fn(x) {
        let u = #(x.0 - 1, x.1)
        let ur = #(x.0 - 1, x.1 + 1)
        let ul = #(x.0 - 1, x.1 - 1)
        let r = #(x.0, x.1 + 1)
        let l = #(x.0, x.1 - 1)
        let d = #(x.0 + 1, x.1)
        let dr = #(x.0 + 1, x.1 + 1)
        let dl = #(x.0 + 1, x.1 - 1)

        [[u, ur, r], [r, dr, d], [u, ul, l], [l, dl, d]]
        |> list.map(fn(xs) {
          case list.map(xs, list.contains(region, _)) {
            [False, _, False] | [True, False, True] -> 1
            _ -> 0
          }
        })
        |> int.sum
      })
      |> list.fold(0, int.add)

    corners * list.length(region)
  })
}

pub fn main() {
  let file = "./resources/day12.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)

  io.println("Day 12 | Part 1: " <> part1)
  io.println("Day 12 | Part 2: " <> part2)
}
