package deques.palindrome;

import deques.Deque;
import deques.LinkedDeque;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            deque.addLast(ch);
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque);
    }

    private boolean isPalindrome(Deque<Character> deque) {
        if (deque.size() < 2) {
            return true;
        } else {
            if (deque.removeFirst() != deque.removeLast()) {
                return false;
            } else {
                return isPalindrome(deque);
            }
        }
    }


    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque, cc);
    }

    public boolean isPalindrome(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() < 2) {
            return true;
        } else {
            if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
                return false;
            } else {
                return isPalindrome(deque, cc);
            }
        }
    }
}
