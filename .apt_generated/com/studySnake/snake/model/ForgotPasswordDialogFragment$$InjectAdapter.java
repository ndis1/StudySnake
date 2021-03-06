// Code generated by dagger-compiler.  Do not edit.
package com.studySnake.snake.model;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<ForgotPasswordDialogFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code ForgotPasswordDialogFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<ForgotPasswordDialogFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<ForgotPasswordDialogFragment>} and handling injection
 * of annotated fields.
 */
public final class ForgotPasswordDialogFragment$$InjectAdapter extends Binding<ForgotPasswordDialogFragment>
    implements Provider<ForgotPasswordDialogFragment>, MembersInjector<ForgotPasswordDialogFragment> {
  private Binding<UserManager> userM;

  public ForgotPasswordDialogFragment$$InjectAdapter() {
    super("com.studySnake.snake.model.ForgotPasswordDialogFragment", "members/com.studySnake.snake.model.ForgotPasswordDialogFragment", NOT_SINGLETON, ForgotPasswordDialogFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    userM = (Binding<UserManager>) linker.requestBinding("com.studySnake.snake.model.UserManager", ForgotPasswordDialogFragment.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(userM);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<ForgotPasswordDialogFragment>}.
   */
  @Override
  public ForgotPasswordDialogFragment get() {
    ForgotPasswordDialogFragment result = new ForgotPasswordDialogFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<ForgotPasswordDialogFragment>}.
   */
  @Override
  public void injectMembers(ForgotPasswordDialogFragment object) {
    object.userM = userM.get();
  }
}
