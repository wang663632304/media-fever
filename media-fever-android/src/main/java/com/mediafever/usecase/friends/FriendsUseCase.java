package com.mediafever.usecase.friends;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.UserImpl;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendsUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendsRepository friendsRepository;
	private Long userId;
	private List<UserImpl> friends = Lists.newArrayList();
	
	@Inject
	public FriendsUseCase(APIService apiService, FriendsRepository friendsRepository) {
		super(apiService);
		this.friendsRepository = friendsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (friendsRepository.isOutdated()) {
			List<UserImpl> friends = getApiService().getFriends(userId);
			friendsRepository.replaceAll(friends);
			friendsRepository.refreshUpdateTimestamp();
		}
		friends = friendsRepository.getAll();
	}
	
	public List<UserImpl> getFriends() {
		return friends;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
