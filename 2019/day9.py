# import os

# path = os.path.join(os.getcwd(), "data", "input_9a.txt")
# with open(path, "r") as f:
#     data = f.readlines()

puzzle_input = [1102,34463338,34463338,63,1007,63,34463338,63,1005,63,53,1102,3,1,1000,109,988,209,12,9,1000,209,6,209,3,203,0,1008,1000,1,63,1005,63,65,1008,1000,2,63,1005,63,904,1008,1000,0,63,1005,63,58,4,25,104,0,99,4,0,104,0,99,4,17,104,0,99,0,0,1102,521,1,1028,1101,0,36,1000,1102,30,1,1005,1101,21,0,1013,1101,26,0,1006,1102,31,1,1017,1101,24,0,1007,1101,0,1,1021,1102,27,1,1019,1101,23,0,1010,1101,0,38,1012,1102,35,1,1001,1101,25,0,1003,1102,20,1,1004,1101,0,37,1009,1101,424,0,1023,1102,39,1,1008,1102,406,1,1027,1102,1,413,1026,1101,0,29,1002,1102,1,0,1020,1102,34,1,1014,1102,1,28,1018,1102,1,33,1011,1102,300,1,1025,1102,1,22,1015,1102,305,1,1024,1101,32,0,1016,1102,427,1,1022,1101,512,0,1029,109,14,1205,6,197,1001,64,1,64,1106,0,199,4,187,1002,64,2,64,109,-18,1207,8,19,63,1005,63,215,1105,1,221,4,205,1001,64,1,64,1002,64,2,64,109,10,1208,-1,28,63,1005,63,237,1106,0,243,4,227,1001,64,1,64,1002,64,2,64,109,-2,2102,1,0,63,1008,63,22,63,1005,63,263,1105,1,269,4,249,1001,64,1,64,1002,64,2,64,109,11,21107,40,39,0,1005,1015,289,1001,64,1,64,1106,0,291,4,275,1002,64,2,64,109,9,2105,1,0,4,297,1105,1,309,1001,64,1,64,1002,64,2,64,109,-13,2101,0,-5,63,1008,63,25,63,1005,63,329,1105,1,335,4,315,1001,64,1,64,1002,64,2,64,109,1,1206,8,353,4,341,1001,64,1,64,1105,1,353,1002,64,2,64,109,3,2108,37,-6,63,1005,63,375,4,359,1001,64,1,64,1106,0,375,1002,64,2,64,109,-16,1207,2,36,63,1005,63,397,4,381,1001,64,1,64,1105,1,397,1002,64,2,64,109,28,2106,0,0,1001,64,1,64,1106,0,415,4,403,1002,64,2,64,109,-3,2105,1,-1,1106,0,433,4,421,1001,64,1,64,1002,64,2,64,109,-12,2108,25,-6,63,1005,63,449,1105,1,455,4,439,1001,64,1,64,1002,64,2,64,109,-19,1202,8,1,63,1008,63,38,63,1005,63,479,1001,64,1,64,1105,1,481,4,461,1002,64,2,64,109,14,2107,25,0,63,1005,63,497,1105,1,503,4,487,1001,64,1,64,1002,64,2,64,109,24,2106,0,-3,4,509,1001,64,1,64,1105,1,521,1002,64,2,64,109,-20,1208,-2,37,63,1005,63,543,4,527,1001,64,1,64,1106,0,543,1002,64,2,64,109,7,21102,41,1,0,1008,1018,43,63,1005,63,563,1105,1,569,4,549,1001,64,1,64,1002,64,2,64,109,-7,1205,10,587,4,575,1001,64,1,64,1106,0,587,1002,64,2,64,109,-11,1202,5,1,63,1008,63,30,63,1005,63,609,4,593,1106,0,613,1001,64,1,64,1002,64,2,64,109,4,1201,5,0,63,1008,63,34,63,1005,63,637,1001,64,1,64,1105,1,639,4,619,1002,64,2,64,109,12,1206,5,651,1105,1,657,4,645,1001,64,1,64,1002,64,2,64,109,9,21101,42,0,-7,1008,1018,39,63,1005,63,677,1105,1,683,4,663,1001,64,1,64,1002,64,2,64,109,-2,21101,43,0,-8,1008,1015,43,63,1005,63,705,4,689,1106,0,709,1001,64,1,64,1002,64,2,64,109,-25,2107,38,10,63,1005,63,727,4,715,1106,0,731,1001,64,1,64,1002,64,2,64,109,7,2102,1,2,63,1008,63,24,63,1005,63,757,4,737,1001,64,1,64,1105,1,757,1002,64,2,64,109,-13,1201,10,0,63,1008,63,29,63,1005,63,779,4,763,1105,1,783,1001,64,1,64,1002,64,2,64,109,30,21108,44,41,-3,1005,1019,803,1001,64,1,64,1106,0,805,4,789,1002,64,2,64,109,-2,21102,45,1,-7,1008,1013,45,63,1005,63,827,4,811,1105,1,831,1001,64,1,64,1002,64,2,64,109,-16,21107,46,47,7,1005,1011,849,4,837,1106,0,853,1001,64,1,64,1002,64,2,64,109,9,21108,47,47,0,1005,1013,875,4,859,1001,64,1,64,1106,0,875,1002,64,2,64,109,-10,2101,0,2,63,1008,63,30,63,1005,63,901,4,881,1001,64,1,64,1105,1,901,4,64,99,21102,1,27,1,21102,1,915,0,1106,0,922,21201,1,51805,1,204,1,99,109,3,1207,-2,3,63,1005,63,964,21201,-2,-1,1,21101,942,0,0,1106,0,922,22101,0,1,-1,21201,-2,-3,1,21101,0,957,0,1105,1,922,22201,1,-1,-2,1105,1,968,21201,-2,0,-2,109,-3,2105,1,0]

for _ in range(3000):
    puzzle_input.append(0)

relative_base = 0

def run_intcodes(loc_puzzle_input, loc_input_values, relative_base, debug=False):
    
    def get_data(index, mode):
        return loc_puzzle_input[get_index(index, mode)]

    def get_index(index, mode):
        if mode == "0":
            return loc_puzzle_input[index]
        elif mode == "1":
            return index
        elif mode == "2":
            return relative_base + loc_puzzle_input[index]
        else:
            return ValueError

    idx = 0
    output_value = 0

    while idx < len(loc_puzzle_input) - 1:
        modes = f"{loc_puzzle_input[idx]:0>5}"
        mode_a = modes[2]
        mode_b = modes[1]
        mode_c = modes[0]
        opcode = modes[3:]
        if debug:
            print(f"idx: {idx} - current opcode: {opcode}, {modes}")
        if opcode == "01":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            c = get_index(idx + 3, mode_c)
            r = a + b
            if debug:
                print(f"Opcode {opcode}: Altered {loc_puzzle_input[c]} at {c} to {r}")
            loc_puzzle_input[c] = r
            increment = 4

        elif opcode == "02":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            c = get_index(idx + 3, mode_c)
            r = a * b
            if debug:
                print(f"Opcode {opcode}: Altered {loc_puzzle_input[c]} at {c} to {r}")
            loc_puzzle_input[c] = r
            increment = 4

        elif opcode == "03":
            a = get_index(idx + 1, mode_a)
            input_value = loc_input_values.pop()
            if debug:
                print(
                    f"Opcode {opcode}: idx{idx}: Altered {loc_puzzle_input[a]} at {a} to {input_value}"
                )
            loc_puzzle_input[a] = input_value
            increment = 2

        elif opcode == "04":
            value_a = get_data(idx + 1, mode_a)
            increment = 2
            if debug:
                print(f"Opcode {opcode}: Output is {value_a}")
            output_value = value_a

        elif opcode == "05":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            if a != 0:
                if debug:
                    print(f"Opcode {opcode}: {a} != 0. idx from {idx} to {b}")
                increment = 0
                idx = b
            else:
                if debug:
                    print(f"Opcode {opcode}: {a} == 0. noop")
                increment = 3

        elif opcode == "06":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            if a == 0:
                if debug:
                    print(f"Opcode {opcode}: idx from {idx} to {b}")
                increment = 0
                idx = b
            else:
                increment = 3

        elif opcode == "07":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            c = get_index(idx + 3, mode_c)
            if a < b:
                if debug:
                    print(f"Opcode {opcode}: {a} < {b}, loc {c} from {loc_puzzle_input[c]} to 1")
                loc_puzzle_input[c] = 1
            else:
                if debug:
                    print(f"Opcode {opcode}: {a} !< {b}, loc {c} from {loc_puzzle_input[c]} to 0")
                loc_puzzle_input[c] = 0
            increment = 4

        elif opcode == "08":
            a = get_data(idx + 1, mode_a)
            b = get_data(idx + 2, mode_b)
            c = get_index(idx + 3, mode_c)
            if a == b:
                if debug:
                    print(f"Opcode {opcode}: {a} == {b}, loc {c} from {loc_puzzle_input[c]} to 1")
                loc_puzzle_input[c] = 1
            else:
                if debug:
                    print(f"Opcode {opcode}: {a} != {b}, loc {c} from {loc_puzzle_input[c]} to 1")
                loc_puzzle_input[c] = 0
            increment = 4

        elif opcode == "09":
            a = get_data(idx + 1, mode_a)
            if debug:
                print(f'opcode 9, mode = {mode_a}, value = {a}')
            relative_base += a
            if debug:
                print(f"Relative Base now {relative_base}")
            increment = 2

        elif opcode == "99":
            print(f"Opcode 99: END -> outputting {output_value}")
            return output_value

        idx += increment



run_intcodes(puzzle_input, [1], relative_base, debug=True)

# run_intcodes(puzzle_input, [2], relative_base, debug=False)