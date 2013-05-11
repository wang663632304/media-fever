package com.mediafever.core.domain.watchable.visitor;

import com.mediafever.core.domain.watchable.Watchable;

/**
 * {@link WatchableVisitor} that doesn't add any behavoir.
 * 
 * @author Estefanía Caravatti
 */
public class DummyWatchableVisitor implements WatchableVisitor {
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitNew(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitNew(Watchable watchable) {
		
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitUpdated(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitUpdated(Watchable watchable) {
		
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitDeleted(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitDeleted(Watchable watchable) {
		
	}
	
}
