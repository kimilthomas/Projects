# Name: test_pa1.py
# Author: Dr. Glick
# Date: September 2, 2023
# Description: Tests pa1 for comp 370

import dfa

class Test:
    """ Represents a single test of user DFA class. """
    def __init__(self, dfa_file, file_exists, file_valid, err_message, str_file, ans_file) -> None:
        self.dfa_file = dfa_file
        self.file_exists = file_exists
        self.file_valid = file_valid
        self.err_message = err_message
        self.str_file = str_file
        self.ans_file = ans_file

if __name__ == "__main__":
    # Tests
    tests = [
        Test("dfa1.txt", True, True, None, "str1.txt", "correct1.txt"),
        Test("dfa2.txt", True, True, None, "str2.txt", "correct2.txt"),
        Test("dfa3.txt", True, True, None, "str3.txt", "correct3.txt"),
        Test("dfa4.txt", True, True, None, "str4.txt", "correct4.txt"),
        Test("dfa5.txt", True, True, None, "str5.txt", "correct5.txt"),
        Test("dfa6.txt", True, True, None, "str6.txt", "correct6.txt"),
        Test("dfa7.txt", True, True, None, "str7.txt", "correct7.txt"),
        Test("dfa8.txt", True, True, None, "str8.txt", "correct8.txt"),
        Test("dfa9.txt", True, True, None, "str9.txt", "correct9.txt"),
        Test("dfa10.txt", True, True, None, "str10.txt", "correct10.txt"),
        Test("dfa30.txt", True, True, None, "str30.txt", "correct30.txt"),
        Test("dfa31.txt", True, True, None, "str31.txt", "correct31.txt"),
        Test("dfa11.txt", False, False, "Should raise FileNotFound exception.", None, None),
        Test("dfa12.txt", True, False, "Should raise FileFormatError exception.  Num states not valid.", None, None),
        Test("dfa13.txt", True, False, "Should raise FileFormatError exception.  File is empty.", None, None),
        Test("dfa14.txt", True, False, "Should raise FileFormatError exception.  No symbols in alphabet.", None, None),
        Test("dfa15.txt", True, False, "Should raise FileFormatError exception.  Missing transition.", None, None),
        Test("dfa16.txt", True, False, "Should raise FileFormatError exception.  Transition redefined.", None, None),
        Test("dfa17.txt", True, False, "Should raise FileFormatError exception.  Too many transitions.", None, None),
        Test("dfa18.txt", True, False, "Should raise FileFormatError exception.  Symbol in transition not in alphabet.", None, None),
        Test("dfa19.txt", True, False, "Should raise FileFormatError exception.  State in transition not a valid state.", None, None),
        Test("dfa20.txt", True, False, "Should raise FileFormatError exception.  State in transition not a valid state.", None, None),
        Test("dfa21.txt", True, False, "Should raise FileFormatError exception.  Invalid start state.", None, None),
        Test("dfa22.txt", True, False, "Should raise FileFormatError exception.  Invalid accept state.", None, None),
        Test("dfa23.txt", True, False, "Should raise FileFormatError exception.  Extra content after accept states.", None, None)]
    
    # Run tests
    num_tested = 0
    num_correct = 0
    for i in range(len(tests)):
        num_tested += 1
        print()
        print(f"Testing DFA {tests[i].dfa_file}")
        try:
            # Create DFA
            dfa_i = dfa.DFA(filename = tests[i].dfa_file)

            # Check results
            if not tests[i].file_exists:
                print("  Incorrect.  DFA file does not exist.  Should have raised")
                print(f"  FileNotFound exception")
            elif not tests[i].file_valid:
                print("  Incorrect.  DFA file not valid.  Should have raised")
                print(f"  FileFormatError exception")
            else:
                # Open string file.
                string_file = open(tests[i].str_file)

                # Simulate DFA on test strings
                results = []
                for string in string_file:
                    results.append(dfa_i.simulate(string.strip()))

                # Get correct results
                file = open(tests[i].ans_file)
                correct_results = [True if result == "Accept" else False for result in file.read().split()]

                # Check if correct
                if results == correct_results:
                    num_correct += 1
                    print("  Correct results")
                else:
                    print("  Incorrect results")
                    print(f"  Your results = {results}")
                    print(f"  Correct results = {correct_results}")
        except FileNotFoundError as err:
            if tests[i].file_exists:
                print("  Incorrect.  File exsists but you raise FileNotFound exception")
            else:
                print("  Correct results")
                num_correct += 1
        except dfa.FileFormatError as err:
            if not tests[i].file_exists:
                print("  Incorrect.  File does not exsist but you raise FileFormatError exception")
            elif tests[i].file_valid:
                print("  Incorrect.  File valid but you raise FileFormatError exception")
            else:
                print("  Correct results")
                num_correct += 1
        except Exception as err:
            print(f"  Incorrect.  You raise Exception with message '{err}'")
    
    # Print final results
    print()
    print(f"Num tests = {num_tested}.  Num correct = {num_correct}")
    if num_correct == num_tested:
        print("All correct.  Nice job.")
    else:
        print("One or more incorrect.  Keep working on it")