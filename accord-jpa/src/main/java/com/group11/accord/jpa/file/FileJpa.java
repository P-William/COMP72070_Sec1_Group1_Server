package com.group11.accord.jpa.file;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FileJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    @Enumerated(EnumType.STRING)
    private FileType type;

    @NonNull
    @Column
    private byte[] data;

    public static FileJpa create(FileType type, byte[] data){
        return FileJpa.builder()
                .type(type)
                .data(data)
                .build();
    }
}
