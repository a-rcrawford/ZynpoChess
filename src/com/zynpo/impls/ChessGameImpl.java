package com.zynpo.impls;

import com.zynpo.enums.GameStatus;
import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.SideColor;
import com.zynpo.exceptions.MoveException;
import com.zynpo.interfaces.ChessBoardState;
import com.zynpo.interfaces.ChessGame;
import com.zynpo.interfaces.MoveRecord;
import com.zynpo.interfaces.pieces.Pawn;

import java.util.*;

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
    public GameStatus takeBackLastMove() {
        _moves.remove(_moves.size() - 1);
        _boardStates.remove(_boardStates.size() - 1);

        return _boardStates.get(_boardStates.size() - 1).getGameStatus();
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


    @Override
    public String getAllMoves() {
        StringBuilder sb = new StringBuilder();

        for (MoveRecord move : _moves) {
            sb.append(move.notation());
            sb.append(", ");
        }

        return sb.substring(0, sb.length() - 3);
    }


    @Override
    public void loadAllMoves(String csvMoves) throws MoveException {
        if (_moves.size() != 0) {
            throw new InternalError("ChessGame should only loadAllMoves() when no moves have been made yet.");
        }

        String[] moves = csvMoves.split(",");

        for (String move : moves) {
            doMove(move);
        }
    }


    // TODO: Still have to determine whether the same board state came up 5 times in the last 5 consecutive moves ...
    // Pass in the number of moves to check, to see how many times it came up ...
    // int overHowManyLastMoves = 10
    private int countOfCurrentBoardState() {
        int count = 1;
        ChessBoardState currentState = _boardStates.get(_boardStates.size() - 1);
        int currentStatePieceCount = currentState.getBoard().getPiecesInPlay(PieceFlags.AllPieces).size();

        for (int i = _boardStates.size() - 3; 0 <= i; i -= 2) {
            ChessBoardState earlierState = _boardStates.get(i);
            if (earlierState.equals(currentState)) {
                ++count;
            } else {
                int earlierStatePieceCount = earlierState.getBoard().getPiecesInPlay(PieceFlags.AllPieces).size();

                if (currentStatePieceCount < earlierStatePieceCount) {
                    // There's no way any more earlier board states can equal this one ...
                    break;
                }
            }
        }

        return count;
    }


    private int countOfMovesWithoutProgress() {
        int count;
        for (count = 0; count < _moves.size(); ++count) {
            MoveRecord move = _moves.get(_moves.size() - count - 1);

            if (move.pieceMoved() instanceof Pawn) {
                // Count's over because a pawn moving constitutes progress ...
                break;
            }

            if (null != move.pieceTaken()) {
                // Count's over because a captured piece constitutes progress ...
                break;
            }
        }

        return count;
    }

    @Override
    public Set<SideColor> whoCanForceDraw() {
        Set<SideColor> canForceDrawSet = new TreeSet<>();

        int countOfCurrentBoardState = countOfCurrentBoardState();
        int countOfMovesWithoutProgress = countOfMovesWithoutProgress();

        if (5 <= countOfCurrentBoardState) {
            setOverallGameStatus(GameStatus.DrawByRepetition);
        } else if (3 <= countOfCurrentBoardState) {
            // The player who just moved may force draw ...
            canForceDrawSet.add((_moves.size() & 1) == 1 ? SideColor.White : SideColor.Black);
        }

        if (150 <= countOfMovesWithoutProgress) {
            setOverallGameStatus(GameStatus.DrawByLackOfProgress);
            canForceDrawSet.clear();
        } else if (100 <= countOfMovesWithoutProgress) {
            canForceDrawSet.add(SideColor.White);
            canForceDrawSet.add(SideColor.Black);
        }

        return canForceDrawSet;
    }
}
