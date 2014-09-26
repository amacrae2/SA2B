package chao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryState {

	private static final int NUM_MEM_CARD_SLOTS = 2;
	
	private Map<String, MemoryCard> memoryCards = new HashMap<String, MemoryCard>();
	private Gamecube gc;
	
	public MemoryState(Map<String, MemoryCard> memoryCards, Gamecube gc) {
		this.memoryCards = memoryCards;
		this.gc = gc;
	}
	
	public MemoryState(MemoryCard am, MemoryCard white, MemoryCard blackOne,
			MemoryCard blackTwo, MemoryCard grayOne, MemoryCard grayTwo, Gamecube gc) {
		memoryCards.put(Constants.MC_ONE_NAME, white);
		memoryCards.put(Constants.MC_TWO_NAME, blackOne);
		memoryCards.put(Constants.MC_THREE_NAME, blackTwo);
		memoryCards.put(Constants.MC_FOUR_NAME, am);
		memoryCards.put(Constants.MC_FIVE_NAME, grayOne);
		memoryCards.put(Constants.MC_SIX_NAME, grayTwo);
		this.gc = gc;
	}

	public Gamecube getGc() {
		return gc;
	}
	

	public Set<MemoryCard> getSetOfMemoryCards() {
		Set<MemoryCard> mcs = new HashSet<MemoryCard>();
		for (String key : memoryCards.keySet()) {
			mcs.add(memoryCards.get(key));
		}
		return mcs;
	}
	
	public Map<String, MemoryCard> getMemoryCards() {
		return memoryCards;
	}

	public List<Move> getPossibleMovesList() {
		List<Move> moves = new ArrayList<Move>();
		addMemoryCardMoves(moves);
		addChaoMoves(moves);
		return moves;
	}

	public boolean equals(MemoryState mState) {
		if (!memoryCardsSame(mState)) {
			return false;
		}
		if (!gc.equals(mState.getGc())) {
			return false;
		}
		return true;
	}

	private boolean memoryCardsSame(MemoryState mState) {
		for (MemoryCard mc : mState.getSetOfMemoryCards()) {
			for (MemoryCard mc2 : getSetOfMemoryCards()) {
				if (mc.getName().equals(mc2.getName())) {
					if (!mc.equals(mc2)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void addMemoryCardMoves(List<Move> moves) {
		MemoryCardSlot leftSlot = gc.getLeftSlot();
		MemoryCardSlot rightSlot = gc.getRightSlot();
		addMovesForSlot(moves, leftSlot, rightSlot);
		addMovesForSlot(moves, rightSlot, leftSlot);
	}
	
	private void addChaoMoves(List<Move> moves) {
//		if (!mcsMatchGC(memoryCards, gc)) {
//			System.out.println("Discrepancy between GC and MCs in this state ");
//		}
		MemoryCard mcOne = gc.getLeftSlot().getMc();
		MemoryCard mcTwo = gc.getRightSlot().getMc();
		Set<ChaoGarden> mcOneGardens = gc.getLeftSlot().getMc().getChaoGardenSet();
		Set<ChaoGarden> mcTwoGardens = gc.getRightSlot().getMc().getChaoGardenSet();
		for (ChaoGarden chaoGardenOne : mcOneGardens) {
			Set<String> chaoSetOne = chaoGardenOne.getChaos();
			for (ChaoGarden chaoGardenTwo : mcTwoGardens) {
				Set<String> chaoSetTwo = chaoGardenTwo.getChaos();
				for (String chaoOne : chaoSetOne) {
					for (String chaoTwo : chaoSetTwo) {
						MoveChao move = new MoveChao(mcOne, chaoGardenOne, chaoOne, mcTwo, chaoGardenTwo, chaoTwo);
						moves.add(move);
					}
				}
			}
		}
//		System.out.println("#############################################################################");
//		System.out.println("NumMoves: "+moves.size()+", current state = inserted mcs: "+mcOne.getName()+", "+mcTwo.getName());
//		System.out.println("Curr Mem State:");
//		for (String key : memoryCards.keySet()) {
//			System.out.println(key+": name: "+memoryCards.get(key).getName()+"- Inserted = "+memoryCards.get(key).isInserted()+", Dark Garden:"+memoryCards.get(key).getDarkGarden().getChaos().toString()+", Neutral Garden:"+memoryCards.get(key).getNeutralGarden().getChaos().toString()+", Hero Garden:"+memoryCards.get(key).getHeroGarden().getChaos().toString());
//		}
//		System.out.println("");
//		
//		if (!mcsMatchGC(memoryCards, gc)) {
//			System.out.println("Discrepancy between GC and MCs in this state");
//		}
	}

	private void addMovesForSlot(List<Move> moves, MemoryCardSlot slot, MemoryCardSlot otherSlot) {
//		if (ms.getMoves().size() >= 2 && ms.getMoves().get(ms.getTotalMoves()-1) instanceof MoveMemoryCard && ms.getMoves().get(ms.getTotalMoves()-2) instanceof MoveMemoryCard) {
//			return;
//		}
		if (slot.isEmpty()) {
			for (MemoryCard mc : getSetOfMemoryCards()) {
				MoveMemoryCard move = new MoveMemoryCard(mc,null);
				moves.add(move);
			}
		} else {
			for (MemoryCard mc : getSetOfMemoryCards()) {
				if (!mc.getName().equals(slot.getMc().getName()) && !mc.getName().equals(otherSlot.getMc().getName())) {
					MoveMemoryCard move = new MoveMemoryCard(mc,slot.getMc());
					moves.add(move);
				}
			}
		}
	}

	public MemoryState getNextState(Move move) throws ChaoSwapException {
		if (!mcsMatchGC(memoryCards, gc)) {
			System.out.println("Discrepancy between GC and MCs in this state ");
		}
		Map<String, MemoryCard> mcsNew = new HashMap<String, MemoryCard>();
		Gamecube gcNew = null;
		if (move instanceof MoveMemoryCard) { // if change memory cards   
			gcNew = makeMoveMemoryCardUpdates(move, mcsNew, gcNew); 
			
			
		} else if (move instanceof MoveChao) { // if swap chao
			// make MoveChao updates
			gcNew = new Gamecube(gc.getLeftSlot().makeCopy(), gc.getRightSlot().makeCopy());
			mcsNew = updateMemoryCards((MoveChao) move);
			for (String mcName : mcsNew.keySet()) {
				if (gc.getLeftSlot().getMc().getName().equals(mcName)) {
					gcNew.getLeftSlot().setMc(mcsNew.get(mcName));
				} else if (gc.getRightSlot().getMc().getName().equals(mcName)) {
					gcNew.getRightSlot().setMc(mcsNew.get(mcName));
				}
			}
			
		} else {
			System.out.println("Error: move not instance of MoveChao or MoveMemoryCard");
		}
		
//		System.out.println("BEFORE ");
//		System.out.println("Move: "+move.toString());
//		for (String key : memoryCards.keySet()) {
//			System.out.println(key+": name: "+memoryCards.get(key).getName()+"- Inserted = "+memoryCards.get(key).isInserted()+", Dark Garden:"+memoryCards.get(key).getDarkGarden().getChaos().toString()+", Neutral Garden:"+memoryCards.get(key).getNeutralGarden().getChaos().toString()+", Hero Garden:"+memoryCards.get(key).getHeroGarden().getChaos().toString());
//		}
//		System.out.println("");
//		
//		System.out.println("AFTER ");
//		System.out.println("Move: "+move.toString());
//		for (String key : mcsNew.keySet()) {
//			System.out.println(key+": name: "+mcsNew.get(key).getName()+"- Inserted = "+mcsNew.get(key).isInserted()+", Dark Garden:"+mcsNew.get(key).getDarkGarden().getChaos().toString()+", Neutral Garden:"+mcsNew.get(key).getNeutralGarden().getChaos().toString()+", Hero Garden:"+mcsNew.get(key).getHeroGarden().getChaos().toString());
//		}
//		System.out.println("");
		
		if (!mcsMatchGC(mcsNew, gcNew)) {
			System.out.println("Discrepancy between GC and MCs in this state");
		}
		if (!mcsMatchGC(memoryCards, gc)) {
			System.out.println("Discrepancy between GC and MCs in this state");
		}
		
		MemoryState mState = new MemoryState(mcsNew, gcNew);
		return mState;
	}

	public boolean mcsMatchGC(Map<String, MemoryCard> mcs, Gamecube gc) {
		int insertedMCCount = 0;
		for (String key : mcs.keySet()) {
			if (mcs.get(key).isInserted()) {
				insertedMCCount++;
				if (!(mcs.get(key).equals(gc.getLeftSlot().getMc()) || mcs.get(key).equals(gc.getRightSlot().getMc()))) {
					System.out.println("Error: inserted mcs don't match from gcNew and from mcsNew");
					return false;
				}
			}
		}
		if (insertedMCCount != 2) {
			System.out.println("Error: more or less than 2 mcs think they are inserted");
			return false;
		}
		return true;
	}

	private Gamecube makeMoveMemoryCardUpdates(Move move, Map<String, MemoryCard> mcsNew, Gamecube gcNew) {
		MemoryCard mcNew = ((MoveMemoryCard) move).getNewMemoryCard();
		MemoryCard mcOld = ((MoveMemoryCard) move).getOldMemoryCard();
		// update memory cards (just make copies and update if inserted or not)
		for (String key : memoryCards.keySet()) {
			MemoryCard mc = memoryCards.get(key).makeCopy(key);
			updateMCInsertionState(mcNew, mcOld, mc);
			mcsNew.put(key, mc);
		}
		// update Gamecube
		MemoryCardSlot mcSlot = new MemoryCardSlot(mcNew.makeCopy(mcNew.getName()));
		mcSlot.getMc().insert();
		if (gc.getLeftSlot().getMc().equals(mcOld)) {
			gcNew = new Gamecube(mcSlot, gc.getRightSlot().makeCopy());
		} else if (gc.getRightSlot().getMc().equals(mcOld)) {
			gcNew = new Gamecube(gc.getLeftSlot().makeCopy(), mcSlot);
		} else {
			System.out.println("Error: memory card slot error"); 
		}
		return gcNew;
	}

	private void updateMCInsertionState(MemoryCard mcNew, MemoryCard mcOld, MemoryCard mc) {
		if (mc.getName().equals(mcOld.getName())) {
			mc.pullOut();
		} else if (mc.getName().equals(mcNew.getName())) {
			mc.insert();
		}
	}

	private Map<String, MemoryCard> updateMemoryCards(MoveChao move) throws ChaoSwapException {
		Map<String,MemoryCard> mcsNew = new HashMap<String,MemoryCard>();
		List<MemoryCard> insertedMCs = new ArrayList<MemoryCard>();
		for (String key : memoryCards.keySet()) {
			MemoryCard mc = memoryCards.get(key).makeCopy(key);
			if (mc.isInserted()) {
				insertedMCs.add(mc);
				boolean swapWorked = mc.swapChao(move);
				if (!swapWorked) {
					throw new ChaoSwapException();
				}
			}
			mcsNew.put(key, mc);
		}
		if (insertedMCs.size() != NUM_MEM_CARD_SLOTS) {
			System.out.println("Error: number of inserted memory cards is: " + insertedMCs.size());
		}
		return mcsNew;
	}

	public int getScore(MemoryState finalState) {
		int score = 0;
		for (String key : memoryCards.keySet()) {
			score += memoryCards.get(key).getScore(finalState.getMemoryCards().get(key));
		}
		if (this.equals(finalState)) {
			score += Constants.FINDS_GOAL_STATE;
		}
		return score;
	}
	
	public String toString() {
		String result = "";
		result += "[gc - "+gc.toString()+"], ";
		for (String key : memoryCards.keySet()) {
			result += "[mcs - "+memoryCards.get(key).toString()+"]";
		}
		return result;
	}
	
	public MemoryState createCopy() {
		return new MemoryState(memoryCards.get(Constants.MC_FOUR_NAME).makeCopy(Constants.MC_FOUR_NAME), 
							   memoryCards.get(Constants.MC_ONE_NAME).makeCopy(Constants.MC_ONE_NAME), 
							   memoryCards.get(Constants.MC_TWO_NAME).makeCopy(Constants.MC_TWO_NAME), 
							   memoryCards.get(Constants.MC_THREE_NAME).makeCopy(Constants.MC_THREE_NAME),
							   memoryCards.get(Constants.MC_FIVE_NAME).makeCopy(Constants.MC_FIVE_NAME), 
							   memoryCards.get(Constants.MC_SIX_NAME).makeCopy(Constants.MC_SIX_NAME), 
							   gc.createCopy());
	}
	
}
