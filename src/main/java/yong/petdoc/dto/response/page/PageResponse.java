package yong.petdoc.dto.response.page;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
	List<T> content,
	int currentPage,
	int totalPages,
	long totalElements,
	int size,
	boolean hasNext
) {

	public static <T> PageResponse<T> of(Page<T> page) {
		return new PageResponse<>(
			page.getContent(),
			page.getNumber(),
			page.getTotalPages(),
			page.getTotalElements(),
			page.getSize(),
			page.hasNext()
		);
	}
}
