package io.cat.ai.asyncpp.util;

import lombok.*;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Pair<F, S> {
  private F first;
  private S second;
}