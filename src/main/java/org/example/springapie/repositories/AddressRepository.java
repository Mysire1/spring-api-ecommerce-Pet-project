package org.example.springapie.repositories;

import org.example.springapie.entities.Address;
import org.springframework.data.repository.CrudRepository;

//це доступ бд, до якоїсь сутності, щоб читати, видаляти, оновлювати та створювати
public interface AddressRepository extends CrudRepository<Address, Long> {
}