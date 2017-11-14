/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.cache;

import edu.eci.arsw.collabhangman.model.game.HangmanGame;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 *
 * @author 2095957
 */
public class HangmanRedisGame extends HangmanGame {

    private final String id;
    private final StringRedisTemplate template;

    public HangmanRedisGame(String word, int id, StringRedisTemplate temp) {
        super(word);
        this.id = String.valueOf(id);
        template = temp;

        //palabra, stado palabra, estado juego, ganador
        template.opsForHash().put(this.id, "word", word);
        template.opsForHash().put(this.id, "guessedword", new String(super.guessedWord));
        template.opsForHash().put(this.id, "gamestatus", false);
        template.opsForHash().put(this.id, "winner", "");

    }

    public HangmanRedisGame(int id,StringRedisTemplate temp) {
         super("");
         this.id=String.valueOf(id);
         template=temp;
         
         
  }
    /**
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    @Override
    public String addLetter(char l) {
        String guessedWord = (String) template.opsForHash().get(id, "guessedWord");
        String palabra = (String) template.opsForHash().get(id, "word");
        char[] guessedWordchar = guessedWord.toCharArray();
        //System.out.println("palabraaa:" + word);
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == l) {
                guessedWordchar[i] = l;
            }
        }
        String value = new String(guessedWordchar);
        template.opsForHash().put(id, "guessedWord", value);
        return value;
    }

    @Override
    public synchronized boolean tryWord(String playerName, String s) {
        String palabra = (String) template.opsForHash().get(id, "word");
        if (s.toLowerCase().equals(palabra)) {
            template.opsForHash().put(id, "winner", playerName);
            template.opsForHash().put(id, "gameFinished", true);
            template.opsForHash().put(id, "guessedword", palabra);
            return true;
        }
        return false;
    }

    @Override
    public boolean gameFinished() {
        return (boolean)template.opsForHash().get(id, "gameFinished");
    }

    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    @Override
    public String getWinnerName() {
        return (String)template.opsForHash().get(id, "winner");
    }

    @Override
    public String getCurrentGuessedWord() {
        return (String)template.opsForHash().get(id, "guessedword");
    }

}
