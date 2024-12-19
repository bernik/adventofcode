import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/regexp
import gleam/result
import gleam/set
import gleam/string
import pprint.{debug as d}
import simplifile

fn parse_input(file) {
  let assert Ok(content) = simplifile.read(file)
  let assert Ok(#(first, second)) = string.split_once(content, on: "\n\n")

  let patterns = first |> string.trim |> string.split(on: ", ")
  let designs = second |> string.split(on: "\n") |> list.map(string.trim)

  #(patterns, designs)
}

fn valid_designs(designs, patterns) {
  let assert Ok(re) =
    patterns
    |> string.join("|")
    |> fn(s) { "^(" <> s <> ")+$" }
    |> regexp.from_string

  designs
  |> list.filter(regexp.check(_, with: re))
}

fn part1(file) {
  let #(patterns, designs) = parse_input(file)

  valid_designs(designs, patterns)
  |> list.length
  |> int.to_string
}

fn do_part2(
  s: String,
  patterns: set.Set(String),
  start: Int,
  cache: dict.Dict(String, Result(Int, Nil)),
) -> #(Result(Int, Nil), dict.Dict(String, Result(Int, Nil))) {
  let length = string.length(s)
  let cache_key = string.slice(s, start, length)
  case dict.get(cache, cache_key) {
    Ok(v) -> #(v, cache)
    Error(Nil) -> {
      case start == length {
        True -> #(Ok(1), cache)
        False -> {
          let #(count, cache) =
            list.range(1, length - start)
            |> list.fold(#(0, cache), fn(acc, length) {
              let w = string.slice(s, at_index: start, length: length)
              case set.contains(patterns, w) {
                False -> acc
                True -> {
                  let #(total, cache) = acc
                  case do_part2(s, patterns, start + length, cache) {
                    #(Error(Nil), cache) -> #(total, cache)
                    #(Ok(count), cache) -> #(total + count, cache)
                  }
                }
              }
            })
          let res = case count {
            0 -> Error(Nil)
            n -> Ok(n)
          }
          #(res, cache |> dict.insert(cache_key, res))
        }
      }
    }
  }
}

fn part2(file) {
  let #(patterns, designs) = parse_input(file)
  let designs = valid_designs(designs, patterns)
  let patterns = set.from_list(patterns)

  let #(res, _) =
    designs
    |> list.fold(#(0, dict.new()), fn(acc, design) {
      let #(res, cache) = acc
      let #(count, cache) = do_part2(design, patterns, 0, cache)
      let count = result.unwrap(count, 0)

      #(res + count, cache)
    })

  res |> int.to_string
}

pub fn main() {
  let assert "6" = part1("./resources/day19.example.txt")
  let assert "16" = part2("./resources/day19.example.txt")

  let file = "./resources/day19.input.txt"
  let part1 = part1(file)
  let part2 = part2(file)
  io.println("Day 19 | Part 1: " <> part1)
  io.println("Day 19 | Part 2: " <> part2)
}
