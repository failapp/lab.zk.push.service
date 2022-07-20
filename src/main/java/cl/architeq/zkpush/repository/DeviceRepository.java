package cl.architeq.zkpush.repository;

import cl.architeq.zkpush.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer>,
        PagingAndSortingRepository<Device, Integer> {

    public Optional<Device> findBySerialNumber(String serialNumber);

    public Optional<Device> findByMacAddr(String macAddr);

    public Page<Device> findByEnabled(boolean enabled, Pageable pageable);

    public Page<Device> findByCustomerId(String customerId, Pageable pageable);

}
