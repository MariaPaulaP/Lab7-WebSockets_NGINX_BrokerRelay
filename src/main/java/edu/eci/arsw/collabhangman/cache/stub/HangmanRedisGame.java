/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.cache.stub;

import edu.eci.arsw.collabhangman.model.game.HangmanGame;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 *
 * @author 2095957
 */
public class HangmanRedisGame extends HangmanGame {

    public String id;
    public StringRedisTemplate template;

    public HangmanRedisGame(String word, String id, StringRedisTemplate temp) {
        super(word);
        this.id = "game:" + id;
        this.template = temp;

        //palabra, stado palabra, estado juego, ganador
        /*template.opsForHash().put(this.id, "word", word);
        template.opsForHash().put(this.id, "guessedword", new String(super.guessedWord));
        template.opsForHash().put(this.id, "gamestatus", false);
        template.opsForHash().put(this.id, "winner", "");*/
    }

    public HangmanRedisGame(String id, StringRedisTemplate temp) {
        super("");
        this.id = "game:" + id;
        this.template = temp;

    }

    /**
     * @throws edu.eci.arsw.collabhangman.cache.stub.RedisCacheException
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    @Override
    public String addLetter(char l) throws RedisCacheException {
        /*String guessedWord = (String) template.opsForHash().get(id, "guessedWord");
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
        return value;*/
        try {
            String wordS = (String) template.opsForHash().get(id, "word");
            wordS = wordS.toLowerCase();
            String guessedWord2 = (String) template.opsForHash().get(id, "guessedword");

            this.guessedWord = new char[guessedWord2.length()];
            for (int i = 0; i < guessedWord2.length(); i++) {
                this.guessedWord[i] = guessedWord2.charAt(i);
            }

            for (int i = 0; i < wordS.length(); i++) {
                if (wordS.charAt(i) == l) {
                    this.guessedWord[i] = l;
                }
            }
            guessedWord2 = "";
            for (int i = 0; i < this.guessedWord.length; i++) {
                guessedWord2 = guessedWord2 + this.guessedWord[i];
            }
            template.opsForHash().put(id, "guessedword", guessedWord2);
            return new String(this.guessedWord);
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }

    @Override
    public synchronized boolean tryWord(String playerName, String s) throws RedisCacheException {
        try {
            String wordS = (String) template.opsForHash().get(id, "word");
            if (s.toLowerCase().equals(wordS)) {
                winner = playerName;
                gameFinished = true;
                guessedWord = wordS.toCharArray();
                template.opsForHash().put(id, "winner", playerName);
                template.opsForHash().put(id, "gameFinished", true);
                template.opsForHash().put(id, "guessedword", s);
                return true;
            }
            return false;
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }

    @Override
    public boolean gameFinished() throws RedisCacheException {
        try {
            return (boolean) template.opsForHash().get(id, "gameFinished");
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        }
    }

    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    @Override
    public String getWinnerName() {
        return (String) template.opsForHash().get(id, "winner");
    }

    @Override
    public String getCurrentGuessedWord() throws RedisCacheException {
        try {
            String a;
            a = (String) template.opsForHash().get(id, "guessedword");
            System.out.println(a);
            if (a == null) {
                throw new RedisCacheException("El identificador ingresado no existe.");
            } else {
                return a;
            }
        } catch (RedisConnectionFailureException e) {
            throw new RedisCacheException("Se ha perdido la conexión con la base de datos");
        } catch (Exception e) {
            throw new RedisCacheException(e.getMessage());
        }
    }
}
