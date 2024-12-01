package tingeso.mueblesstgo.dtos;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraInformationEmployee {
    private String rut;
    private String name;
    private char category;
    private int amountExtraHours;
    private int amountJustifiedDays;
    private int amountArrears;
}
