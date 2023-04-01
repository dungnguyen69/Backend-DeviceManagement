package devices;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {
	
	@PostMapping(value = "/device")
	public DeviceDTO createNew(@RequestBody DeviceDTO deviceDTO) {
		return deviceDTO;
	}
	
	@PutMapping(value = "/device")
	public DeviceDTO updateNew(@RequestBody DeviceDTO deviceDTO) {
		return deviceDTO;
	}
	
	@DeleteMapping(value = "/device")
	public void deleteNew(@RequestBody long[] ids) {
		
	}
}
