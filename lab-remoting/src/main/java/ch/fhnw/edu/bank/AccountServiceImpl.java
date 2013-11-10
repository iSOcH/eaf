package ch.fhnw.edu.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountServiceImpl implements AccountService, RemoteAccountService {

	private Map<String, List<Account>> map = new HashMap<String, List<Account>>();

	public List<Account> getAccounts(String name) {
		return map.get(name);
	}

	public void insertAccount(Account acc) {
		String name = acc.getName();
		if (map.get(name) == null) {
			map.put(name, new ArrayList<Account>());
		}
		map.get(name).add(acc);
	}

}
