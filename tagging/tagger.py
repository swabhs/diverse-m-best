import sys
from math import *

class Distribution:
  "Store the counts from a corpus."
  def __init__(self, file):
    self.words = {}
    self.ngrams = [None, {}, {}, {}]
    for l in open(file):
      t = l.strip().split()
      count = float(t[0])
      key = tuple(t[2:])
      if t[1] == "1-GRAM": 
        self.ngrams[1][key[0]] = count 
      elif t[1] == "2-GRAM": 
        self.ngrams[2][key] = count 
      elif t[1] == "3-GRAM": 
        self.ngrams[3][key] = count 
      elif t[1] == "WORDTAG": 
        self.words[key] = count
    self.word_counts = {}
    for tag, word in self.words.iterkeys():
      if word not in self.word_counts:
        self.word_counts[word] = sum([self.words.get((tag, word), 0.0) 
                                      for tag in self.tags()])

  def tags(self): 
    "Return the tags in the model."
    return self.ngrams[1].keys() + ["*", "STOP"]

  def word_count(self, word):
    "Return the counts of each word type."
    return self.word_counts.get(word, 0.0)

  def trigram_prob(self, trigram):
    "Return the probability of the trigram given the prefix bigram."
    bigram = trigram[:-1]
    if bigram not in self.ngrams[2]: return 0.0
    return self.ngrams[3].get(trigram, 0.0) / self.ngrams[2][bigram]

  def emission_prob(self, word, tag):
    "Return the probability of the tag emitting the word."
    if tag in ["*", "STOP"] : return 0.0
    new_word = word if self.word_count(word) >= 5 else "_RARE_"
    return self.words.get((tag, new_word), 0.0) / self.ngrams[1][tag]

def replace_rare_words(distribution, sentence):
  "Replace all words seen less than 5 times with the _RARE_ word."
  new_sent = []
  for pair in sentence:
    w, t = pair.split()
    if distribution.word_count(w) < 5:
      new_sent.append("_RARE_ " + t)
    else:
      new_sent.append(pair)
  return new_sent

def viterbi(distribution, sentence):
  "Run the viterbi algorithm over the sentence, assuming a HMM distribution." 
  # Define the variables to be the same as in the class slides.
  n = len(sentence)
  K = distribution.tags()
  x = [""] + sentence
  y = [""] * (n + 1)
  def q(w, u, v): return distribution.trigram_prob((u, v, w))
  def e(x, u): return distribution.emission_prob(x, u)
  def argmax(ls): return max(ls, key = lambda x: x[1])

  # The Viterbi algorithm.
  # Create and initialize the chart.
  pi = [((0, "*", "*"), 1.0)] + \
      [((0, u, v), 0) for u in K for v in K if (u, v) != ("*", "*")]
  pi = dict(pi)
  bp = {}

  # Run the main loop. 
  for k in range(1, n + 1):
    for u in K:
      for v in K:
        ls = [(w, pi[k - 1, w, u] * q(v, w, u) * e(x[k], v)) for w in K]
        bp[k, u, v], pi[k, u, v]  = \
            argmax([(w, pi[k - 1, w, u] * q(v, w, u) * e(x[k], v)) for w in K])  
  # Follow the back pointers in the chart.
  (y[n - 1], y[n]), score  = argmax([((u,v), pi[n, u, v] * q("STOP", u, v)) 
                                 for u in K for v in K]) 
  for k in range(n - 2, 0, -1):
    y[k] = bp[k + 2, y[k + 1], y[k + 2]]
  y[0] = "*"
  scores = [pi[i, y[i - 1], y[i]] for i in range(1, n)] 
  return y[1:n + 1], scores + [score]

def main(mode, count_file, sentence_file):
  distribution = Distribution(count_file)
  sentence = []
  for l in open(sentence_file):
    if l.strip():
      sentence.append(l.strip())
    else:
      if mode == "TAG":
        tagging, scores = viterbi(distribution, sentence)
        print "\n".join([w + " " + t + " " + str(log(s)) for w, t, s in zip(sentence, tagging, scores)])
      elif mode == "REPLACE":
        new_sent = replace_rare_words(distribution, sentence)
        print "\n".join(new_sent)
      else: 
        print "Bad mode:", mode
      print
      sentence = []

if __name__ == '__main__': main(sys.argv[1], sys.argv[2], sys.argv[3])
