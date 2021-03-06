package impactotecnologico.challenge.pooling.car.rest.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import impactotecnologico.challenge.pooling.car.models.Group;
import impactotecnologico.challenge.pooling.car.rest.exceptions.ProcessingDataException;
import impactotecnologico.challenge.pooling.car.services.GroupService;
import impactotecnologico.challenge.pooling.car.utils.Verifications;

@RestController
public class JourneyRestController extends AbstractController {

	@Autowired
	GroupService groupService;

	@PostMapping("/journey")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Void> registerGroup(@RequestBody Group group) throws IllegalArgumentException {
		Verifications.checkIfNotNull(group);

		if (group.getExternalId() <= 0 || group.getPeople() <= 0) {
			throw new IllegalArgumentException();
		}

		Optional<Group> groupSaved = this.groupService.registerGroupForJourney(group);

		if (groupSaved.isPresent()) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new ProcessingDataException();
		}
	}

	@RequestMapping(path = "/dropoff", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> unregisterGroup(@RequestParam(name = "ID") Integer id) throws IllegalArgumentException {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException();
		}

		this.groupService.unregisterGroupById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
