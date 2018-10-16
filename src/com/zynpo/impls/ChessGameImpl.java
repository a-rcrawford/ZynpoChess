package com.zynpo.impls;

import com.zynpo.enums.GameStatus;
import com.zynpo.enums.SideColor;
import com.zynpo.exceptions.MoveException;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessBoardState;
import com.zynpo.interfaces.ChessGame;
import com.zynpo.interfaces.MoveRecord;

import java.util.ArrayList;
import java.util.List;

public class ChessGameImpl implements ChessGame {

    private List<ChessBoardState> _boardStates;
    private List<MoveRecord> _moves;
    private int _currentViewIndex;


    public ChessGameImpl() {
        _boardStates = new ArrayList<>();
        _boardStates.add(new ChessBoardStateImpl(new ChessBoardImpl(), SideColor.White, true));
        _moves = new ArrayList<>();
        _currentViewIndex = 0;
    }


    @Override
    public boolean piecesCanDropBackIntoPlay() {
        return false;
    }


    @Override
    public boolean takenPiecesSwitchSides() {
        return false;
    }


    @Override
    public GameStatus doMove(String notation) throws MoveException {

        if(this.getOverallGameStatus().meansGameIsOver()) {
            throw new InternalError("Can't do move \""
                    + notation + "\" when game is already over: " + this.getOverallGameStatus());
        }

        ChessBoardState currentBoardState = _boardStates.get(_boardStates.size() - 1);
        SideColor sideToMove = (0 == (_boardStates.size() & 1)) ? SideColor.White : SideColor.Black;

        MoveRecord moveRecord = MoveRecordImpl.fromNotation(notation, sideToMove, currentBoardState.getBoard());

        _moves.add(moveRecord);
        _boardStates.add(moveRecord.resultingBoardState());
        this.setOverallGameStatus(moveRecord.resultingBoardState().getGameStatus());

        return moveRecord.gameStatus();
    }


    @Override
    public void takeBackLastMove() {
        _moves.remove(_moves.size() - 1);
        _boardStates.remove(_boardStates.size() - 1);
    }


    @Override
    public ChessBoardState reviewFirst() {
        _currentViewIndex = 0;
        return _boardStates.get(_currentViewIndex);
    }


    @Override
    public ChessBoardState reviewNext() throws IndexOutOfBoundsException {
        if (_currentViewIndex < _boardStates.size() - 1) {
            return _boardStates.get(++_currentViewIndex);
        } else {
            throw new IndexOutOfBoundsException("No more ChessBoardState(s) to reviewNext() on");
        }
    }


    @Override
    public ChessBoardState reviewPrevious() throws IndexOutOfBoundsException {
        if (0 < _currentViewIndex) {
            return _boardStates.get(--_currentViewIndex);
        } else {
            throw new IndexOutOfBoundsException("No more ChessBoardState(s) to reviewPrevious() on");
        }
    }


    @Override
    public ChessBoardState reviewLast() {
        _currentViewIndex = _boardStates.size() - 1;
        return _boardStates.get(_currentViewIndex);
    }
}
