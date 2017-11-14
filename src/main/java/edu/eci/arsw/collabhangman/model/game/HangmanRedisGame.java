/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game;

/**
 *
 * @author 2095957
 */
public class HangmanRedisGame extends HangmanGame {
    
  
    
    
    public HangmanRedisGame(String word) {
        super(word);
    }
    
    /**
     * @pre gameFinished==false
     * @param l new letter
     * @return the secret word with all the characters 'l' revealed
     */
    @Override
    public String addLetter(char l){   
       return null;
    }
    @Override
    public synchronized boolean tryWord(String playerName,String s){
        return false;
    }
    @Override
    public boolean gameFinished(){
        return false;
    }
    
    /**
     * @pre gameFinished=true;
     * @return winner's name
     */
    @Override
    public String getWinnerName(){
        return null;
    }
    @Override
    public String getCurrentGuessedWord(){
        return  null;
    }    
    
}
