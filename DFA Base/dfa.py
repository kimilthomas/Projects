import re
import sys


class FileFormatError(Exception):
    """
    Exception that is raised if the
    input file to the DFA constructor
    is incorrectly formatted.
    """
    pass

class DFA:
    def __init__(self, filename):
        """
        Initializes DFA object from the DFA specification
        in the named parameter filename.
        """
        self.transitions = {}
        self.alphabet = set()
        self.accept_states = set()
        self.start_state = None

        try:
            with open(filename, 'r') as file:
                lines = [line.strip() for line in file.readlines()]  # Strip leading and trailing whitespace

                # Parse the number of states and alphabet
                num_states = int(lines[0])
                alphabet = lines[1]
                self.alphabet = set(alphabet)

                # Define a regular expression pattern for transitions (e.g., "1 '0' 4")
                transition_pattern = re.compile(r'(\d+)\s+\'([01])\'\s+(\d+)')

                # Parse the transitions using the regex pattern
                for line in lines[2:]:
                    match = transition_pattern.match(line)
                    if match:
                        state_from, symbol, state_to = map(int, match.groups())
                        self.transitions[(state_from, symbol)] = state_to
                    else:
                        break  # Stop parsing when an invalid transition format is encountered

                # Initialize start state as None
                self.start_state = None

                # Search for the start state
                start_state_line = lines[-2].strip()
                if start_state_line.isdigit():
                    self.start_state = int(start_state_line)
                else:
                    raise FileFormatError("Start state not found or invalid format")

                # Parse the accept states
                accept_states_line = lines[-1].strip()
                accept_states_parts = accept_states_line.split()
                self.accept_states = set(accept_states_parts)

        except FileNotFoundError:
            raise FileFormatError("File not found")

    def simulate(self, input_string):
        """
        Returns True if input_string is in the language of the DFA,
        and False if not.

        Assumes that all characters in input_string are in the alphabet
        of the DFA.
        """
        current_state = self.start_state
        print(self.transitions)


        for symbol in input_string:
            if (current_state, symbol) in self.transitions:
                            next_state = self.transitions[(current_state, symbol)]
                            current_state = next_state
        if current_state in self.accept_states:
                return True  # After processing the entire input string, check if the final state is valid

        else:            
                return False



if __name__ == "__main__":
    # Check for correct number of command line arguments
    """"
    if len(sys.argv) != 3:
        print("Usage: python3 dfa.py dfa_filename str_filename")
        sys.exit(1)

    dfa_filename = sys.argv[1]
    str_filename = sys.argv[2]
    """
    dfa_filename =  "dfa1.txt"
    str_filename = "str1.txt"

    try:
        dfa = DFA(filename=dfa_filename)

        with open(str_filename, 'r') as str_file:
            for line in str_file:
                input_string = line.strip()  # Strip newline character
                result = dfa.simulate(input_string)
                if result:
                    print(f"Accept")
                else:
                    print(f"Reject")
    except FileFormatError as e:
        print(f"Error: {e}")
        sys.exit(1)




