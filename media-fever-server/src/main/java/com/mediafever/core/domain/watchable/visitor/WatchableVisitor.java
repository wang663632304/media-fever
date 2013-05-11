package com.mediafever.core.domain.watchable.visitor;

import com.mediafever.core.domain.watchable.Watchable;

/**
 * Visitor to add any special behavior related to {@link Watchable}s.
 * 
 * @author Estefanía Caravatti
 */
public interface WatchableVisitor {
	
	/**
	 * Visit a new {@link Watchable}.
	 * 
	 * @param watchable The new {@link Watchable}.
	 */
	public void visitNew(Watchable watchable);
	
	/**
	 * Visit an updated {@link Watchable}.
	 * 
	 * @param watchable The updated {@link Watchable}.
	 */
	public void visitUpdated(Watchable watchable);
	
	/**
	 * Visit a deleted {@link Watchable}.
	 * 
	 * @param watchable The deleted {@link Watchable}.
	 */
	public void visitDeleted(Watchable watchable);
}
