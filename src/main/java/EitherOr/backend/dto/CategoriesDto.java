package EitherOr.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoriesDto {
    List<String> categories;

    public CategoriesDto(List<String> categories) {
        this.categories = categories;
    }

}
