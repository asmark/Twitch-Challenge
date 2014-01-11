import random
import re
import sys

random.seed()

MAX_REPETITIONS = 15
VOWELS = ['a', 'e', 'i', 'o', 'u']
VOWELS_REGEXP = "|".join(VOWELS)

def toggle_case(word):
	start_index = random.randint(0, len(word))
	end_index = random.randint(start_index, len(word))
	return word[0:start_index] + word[start_index:end_index].swapcase() + word[end_index:]

def repeat_letter(word):
	repetition_index = random.randint(0, len(word)-1)
	repetitions = random.randint(0, MAX_REPETITIONS)
	return word[0:repetition_index] + word[repetition_index]*repetitions + word[repetition_index:]

def mangle_vowels(word):
	start_index = random.randint(0, len(word))
	end_index = random.randint(start_index, len(word))

	mangle_substr = word[start_index:end_index]
	mangle_substr_no_vowels = re.split(VOWELS_REGEXP, mangle_substr)
	rand_vowel = VOWELS[random.randint(0, len(VOWELS)-1)]
	
	mangled_substr = rand_vowel.join(mangle_substr_no_vowels)
	return word[0:start_index] + mangled_substr + word[end_index:]

def generate_misspelled_word(correct_word):
	misspelled_word = correct_word
	misspell_strategy = []
	for strategy in [toggle_case, mangle_vowels, repeat_letter]:
		if random.randint(0, 3) > 2:
			misspell_strategy.append(strategy)

	random.shuffle(misspell_strategy)

	for strategy in misspell_strategy:
		for i in range(random.randint(0, 5)):
			misspelled_word = strategy(misspelled_word)

	return misspelled_word


word_file = sys.argv[1] if len(sys.argv) >= 2 else "words.txt"
correct_word_list = [word.strip() for word in open(word_file)]
misspelled_word_list = [generate_misspelled_word(word) for word in correct_word_list]

for misspelled_word in misspelled_word_list:
	print misspelled_word
